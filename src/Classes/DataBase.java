package Classes;

import java.sql.*;

public class DataBase {
    private final String url;
    private final String user;
    private final String pass;

    // construtor sem parâmetros → configs fixas
    public DataBase() {
        this.url = "jdbc:mysql://localhost:3306/finances?useSSL=false";
        this.user = "root";
        this.pass = ""; // se tiver senha, configure aqui
    }

    // abrir conexão
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    // criar tabelas (igual estava antes, mantive do seu código)
    public void createTables() {
        String[] sqls = {
            // 1) Address
            """
            CREATE TABLE IF NOT EXISTS address (
                id INT AUTO_INCREMENT PRIMARY KEY,
                street VARCHAR(100) NOT NULL DEFAULT '',
                `number` VARCHAR(20) NOT NULL DEFAULT '',
                neighborhood VARCHAR(100) NOT NULL DEFAULT '',
                city VARCHAR(100) NOT NULL DEFAULT '',
                uf VARCHAR(2) NOT NULL DEFAULT '',
                country VARCHAR(100) NOT NULL DEFAULT '',
                zip_code VARCHAR(20) NOT NULL DEFAULT '',
                UNIQUE (street, `number`, neighborhood, city, uf, country, zip_code)
            ) ENGINE=InnoDB;
            """,

            // 2) Contact
            """
            CREATE TABLE IF NOT EXISTS contact (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL DEFAULT '',
                email VARCHAR(100) NOT NULL DEFAULT '' UNIQUE,
                phone VARCHAR(30) NOT NULL DEFAULT '',
                date_of_birth DATE,
                cpf VARCHAR(20) NOT NULL DEFAULT '' UNIQUE,
                address_id INT NOT NULL,
                FOREIGN KEY (address_id) REFERENCES address(id) ON UPDATE CASCADE ON DELETE RESTRICT
            ) ENGINE=InnoDB;
            """,

            // 3) Client
            """
            CREATE TABLE IF NOT EXISTS client (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL DEFAULT '' UNIQUE,
                password VARCHAR(255) NOT NULL DEFAULT '',
                contact_id INT NOT NULL,
                FOREIGN KEY (contact_id) REFERENCES contact(id) ON UPDATE CASCADE ON DELETE RESTRICT
            ) ENGINE=InnoDB;
            """,

            // 4) Admin (one-to-one com client)
            """
            CREATE TABLE IF NOT EXISTS admin (
                client_id INT PRIMARY KEY,
                FOREIGN KEY (client_id) REFERENCES client(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 5) Capital
            """
            CREATE TABLE IF NOT EXISTS capital (
                id INT AUTO_INCREMENT PRIMARY KEY,
                client_id INT NOT NULL,
                name VARCHAR(50),
                balance DECIMAL(12,2) DEFAULT 0.00,
                FOREIGN KEY (client_id) REFERENCES client(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 6) Current (vincula capital -> current)
            """
            CREATE TABLE IF NOT EXISTS current (
                id INT AUTO_INCREMENT PRIMARY KEY,
                capital_id INT NOT NULL UNIQUE,
                FOREIGN KEY (capital_id) REFERENCES capital(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 7) SafeZone (vincula current -> safezone)
            """
            CREATE TABLE IF NOT EXISTS safezone (
                id INT AUTO_INCREMENT PRIMARY KEY,
                current_id INT NOT NULL UNIQUE,
                interest_rate DOUBLE NOT NULL,
                FOREIGN KEY (current_id) REFERENCES current(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 8) Loan (vincula safezone -> loan). inclui destination_id para o destino das transferências de empréstimo
            """
            CREATE TABLE IF NOT EXISTS loan (
                id INT AUTO_INCREMENT PRIMARY KEY,
                safezone_id INT NOT NULL UNIQUE,
                initial_fee DOUBLE NOT NULL,
                destination_id INT,
                FOREIGN KEY (safezone_id) REFERENCES safezone(id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY (destination_id) REFERENCES capital(id) ON UPDATE CASCADE ON DELETE SET NULL
            ) ENGINE=InnoDB;
            """,

            // 9) Operation (base para deposit/withdraw/transfer)
            """
            CREATE TABLE IF NOT EXISTS operation (
                id INT AUTO_INCREMENT PRIMARY KEY,
                datetime DATETIME NOT NULL,
                value DECIMAL(12,2) NOT NULL,
                capital_id INT NOT NULL,
                FOREIGN KEY (capital_id) REFERENCES capital(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 10) Deposit (subtipo de operation)
            """
            CREATE TABLE IF NOT EXISTS deposit (
                operation_id INT PRIMARY KEY,
                FOREIGN KEY (operation_id) REFERENCES operation(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 11) Withdraw (subtipo de operation)
            """
            CREATE TABLE IF NOT EXISTS withdraw (
                operation_id INT PRIMARY KEY,
                FOREIGN KEY (operation_id) REFERENCES operation(id) ON UPDATE CASCADE ON DELETE CASCADE
            ) ENGINE=InnoDB;
            """,

            // 12) Transfer (subtipo de operation, guarda destination_id)
            """
            CREATE TABLE IF NOT EXISTS transfer (
                operation_id INT PRIMARY KEY,
                destination_id INT NOT NULL,
                FOREIGN KEY (operation_id) REFERENCES operation(id) ON UPDATE CASCADE ON DELETE CASCADE,
                FOREIGN KEY (destination_id) REFERENCES capital(id) ON UPDATE CASCADE ON DELETE RESTRICT
            ) ENGINE=InnoDB;
            """,

            // 13) Settings (admin configuration)
            """
            CREATE TABLE IF NOT EXISTS settings (
                sz_interest_rate DOUBLE,
                lo_interest_rate DOUBLE,
                lo_initial_fee DOUBLE
            ) ENGINE=InnoDB;
            """,

            // 14) Settings (initial config)
            """
            INSERT INTO settings (sz_interest_rate, lo_interest_rate, lo_initial_fee)
            SELECT 0, 0, 0
            WHERE (SELECT COUNT(*) FROM settings) = 0;
            """,

            // 15) VIEW clients
            """
            CREATE OR REPLACE VIEW clients AS
            SELECT c.id,
                c.username,
                c.password,
                c.contact_id,
                a.client_id AS admin_id
            FROM client c
            LEFT JOIN admin a ON c.id = a.client_id;
            """,

            // 15) VIEW capitals
            """
            CREATE OR REPLACE VIEW capitals AS
            SELECT ca.id, ca.client_id, ca.balance,
                cu.capital_id as current_id,
                sz.current_id as safezone_id, sz.interest_rate,
                lo.safezone_id as loan_id, lo.initial_fee
            FROM capital ca
            LEFT JOIN current cu ON ca.id = cu.capital_id
            LEFT JOIN safezone sz ON cu.id = sz.current_id
            LEFT JOIN loan lo ON sz.id = lo.safezone_id;
            """,

            // 15) VIEW operations
            """
            CREATE OR REPLACE VIEW operations AS
            SELECT o.id, o.datetime, o.value, o.capital_id,
                d.operation_id as deposit_id,
                w.operation_id as withdraw_id,
                t.operation_id as transfer_id, t.destination_id
            FROM operation o
            LEFT JOIN deposit d ON o.id = d.operation_id
            LEFT JOIN withdraw w ON o.id = w.operation_id
            LEFT JOIN transfer t ON o.id = t.operation_id;
            """
        };

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            for (String sql : sqls) {
                stmt.execute(sql);
            }
            System.out.println("Banco e tabelas criados/verificados com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // executar INSERT/UPDATE/DELETE
    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }
    }

    // executar INSERT que retorna o ID gerado
    public int executeInsert(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Falha ao inserir registro: nenhum ID gerado.");
    }

    // executar SELECT e retornar ResultSet
    public ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }

        // ❗ aqui não fecho a conexão nem o ps,
        // porque o ResultSet precisa ser lido fora
        return ps.executeQuery();
    }
}
