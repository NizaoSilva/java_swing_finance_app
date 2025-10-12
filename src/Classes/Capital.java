package Classes;

import java.sql.*;
import java.util.*;

public abstract class Capital {
    protected final int id;
    protected double balance;
    protected final int client_id;
    protected final List<Operation> operations = new ArrayList<>();

    protected Capital(int id, double balance, int client_id) {
        this.id = id; this.balance = balance; this.client_id = client_id;
    }
    
    public static Capital fromId(int id) throws SQLException {
        if (Current.cache.containsKey(id)) return Current.cache.get(id);
        if (SafeZone.cache.containsKey(id)) return SafeZone.cache.get(id);
        if (Loan.cache.containsKey(id)) return Loan.cache.get(id);
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM capital ca LEFT JOIN current cu ON ca.id = cu.capital_id "
            + "LEFT JOIN safezone sz ON cu.id = sz.current_id LEFT JOIN loan lo ON sz.id = lo.safezone_id WHERE ca.id=?", id)) {
            if (rs.getInt("lo.id") > 0) {
                Loan lo = new Loan(id, rs.getDouble("balance"), rs.getDouble("interest_rate"),
                        rs.getDouble("initial_fee"), rs.getInt("client_id"), rs.getInt("destination_id"));
                Loan.cache.put(id, lo);
                return lo;
            } else if (rs.getInt("sz.id") > 0) {
                SafeZone sz = new SafeZone(id, rs.getDouble("balance"), rs.getDouble("interest_rate"), 
                        rs.getInt("client_id"));
                SafeZone.cache.put(id, sz);
                return sz;
            } else if (rs.getInt("cu.id") > 0) {
                Current cu = new Current(id,rs.getDouble("ca.balance"),
                        rs.getInt("ca.client_id"));
                Current.cache.put(id, cu);
                return cu;
            }
        }
        return null;       
    }
    
    // 🔹 Atualiza lista de operações do banco
    public void refreshOperations() throws SQLException {
        operations.clear();
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM operation o LEFT JOIN deposit d ON o.id = d.operation_id "
                + "LEFT JOIN withdraw w ON o.id = w.operation_id LEFT JOIN transfer t ON o.id = t.operation_id WHERE capital_id=? ORDER BY datetime ASC",this.id)) {
            while (rs.next()) {
                if (rs.getInt("d.operation_id") > 0) {
                    if (Deposit.cache.containsKey(rs.getInt("o.id"))) {
                        operations.add(Deposit.cache.get(rs.getInt("o.id")));
                    } else {
                        Deposit d = new Deposit(rs.getInt("o.id"), rs.getDouble("value"), rs.getTimestamp("datetime").toLocalDateTime(),
                                rs.getInt("deposit_id"));
                        Deposit.cache.put(rs.getInt("o.id"), d);
                        operations.add(d);
                    }
                } else if (rs.getInt("w.operation_id") > 0) {
                    if (Withdraw.cache.containsKey(rs.getInt("o.id"))) {
                        operations.add(Withdraw.cache.get(rs.getInt("o.id")));
                    } else {
                        Withdraw w = new Withdraw(rs.getInt("o.id"), rs.getDouble("value"), rs.getTimestamp("datetime").toLocalDateTime(),
                                rs.getInt("deposit_id"));
                        Withdraw.cache.put(rs.getInt("o.id"), w);
                        operations.add(w);
                    }
                } else if (rs.getInt("t.operation_id") > 0) {
                    if (Transfer.cache.containsKey(rs.getInt("o.id"))) {
                        operations.add(Transfer.cache.get(rs.getInt("o.id")));
                    } else {
                        Transfer t = new Transfer(rs.getInt("o.id"), rs.getDouble("value"), rs.getTimestamp("datetime").toLocalDateTime()
                                , rs.getInt("deposit_id"), rs.getInt("destination_id"));
                        Transfer.cache.put(rs.getInt("o.id"), t);
                        operations.add(t);
                    }
                }
                
                Operation op = Operation.fromId(rs.getInt("id"));
                if (op != null) {
                    operations.add(op);
                }
            }           
        }
    }

    public int getId() { return id; }
    public Client getClient() throws SQLException {return Client.fromId(client_id);}
    public abstract double getBalance() throws SQLException;
    public List<Operation> getOperations() {return operations;}
    
    protected Capital setBalance(double value) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE capital SET balance=? WHERE id=?", value, getId());
        this.balance = value;
        return this;
    }
    
    @Override
    public String toString() {
        if (this instanceof Loan) {
            return this.getId() + ".Loan";
        } else if (this instanceof SafeZone) {
            return this.getId() + ".SafeZone";
        } else if (this instanceof Current) {
            return this.getId() + ".Current";
        } else {
            return this.getId() + ".Capital"; // caso padrão
        }
    }
}
