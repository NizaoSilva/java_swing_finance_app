package Classes;
import java.sql.*;
import java.util.*;

public class Client {
    protected static final Map<Integer, Client> cache = new HashMap<>();
    private final int id;
    private String username, password;
    private int contact_id;
    private final List<Capital> capitals = new ArrayList<>(); 

    protected Client(int id, String username, String password, int contact_id) {
        this.id = id; this.username = username; this.password = password; this.contact_id = contact_id;}

    public static Client create(String username, String password, int contact_id) throws SQLException {
        DataBase db = new DataBase();
        int id = db.executeInsert("INSERT INTO client (username, password, contact_id) VALUES (?, ?, ?)",
            username, password, contact_id);
        Client c = new Client(id, username, password, contact_id);
        cache.put(id, c);
        if (Contact.cache.containsKey(contact_id)) {Contact.fromId(contact_id).getClients().add(c);}
        return c;
    }

    public static Client fromId(int id) throws SQLException {
        if (Admin.cache.containsKey(id)) return Admin.cache.get(id);
        if (cache.containsKey(id)) return cache.get(id);
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM client c LEFT JOIN admin a ON id = client_id WHERE id = ?", id)) {
            if (rs.next()) {
                if (rs.getInt("client_id") > 0) {
                    Admin a = new Admin(id, rs.getString("username"), rs.getString("password"), 
                            rs.getInt("contact_id"));
                    Admin.cache.put(id, a);
                    return a;
                } else {
                    Client c = new Client(id, rs.getString("username"), rs.getString("password"), 
                            rs.getInt("contact_id"));
                    cache.put(id, c);
                    return c;
                }
            }
        }
        return null;
    }
    
    public static Client fromUsername(String username) throws SQLException {
        for (Client c : cache.values()) {
            if (username.equals(c.getUsername())) {
                return c;
            }
        }
        for (Admin a : Admin.cache.values()) {
            if (username.equals(a.getUsername())) {
                return a;
            }
        }
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM client c LEFT JOIN admin a ON id = client_id WHERE username = ?", username)) {
            if (rs.next()) {
                if (rs.getInt("client_id") > 0) {
                    Admin a = new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("password"), 
                            rs.getInt("contact_id"));
                    Admin.cache.put(rs.getInt("id"),  a);
                    return a;
                } else {
                    Client c = new Client(rs.getInt("id"), rs.getString("username"), rs.getString("password"), 
                            rs.getInt("contact_id"));
                    cache.put(rs.getInt("id"),  c);
                    return c;
                }
            }
        }
        return null;
    }
    
    public void refreshCapitals() throws SQLException {
        capitals.clear();
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM capital ca LEFT JOIN current cu ON ca.id = cu.capital_id "
            + "LEFT JOIN safezone sz ON cu.id = sz.current_id LEFT JOIN loan lo ON sz.id = lo.safezone_id WHERE ca.client_id=?", getId())) {
        /* --- Debug ---
        java.sql.ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        System.out.println("\n--- Resultado da query ---");
        int row = 0;
        //--- Debug ---*/
            while (rs.next()) {
            /* --- Debug ---
            row++;
            System.out.print("Linha " + row + ": ");
            for (int i = 1; i <= columnCount; i++) {
                String colName = meta.getColumnName(i);
                Object value = rs.getObject(i);
                System.out.print(colName + "=" + value + " | ");}
            System.out.println();
            // --- Debug --- */
                if (rs.getInt("lo.id") > 0){
                    if (Loan.cache.containsKey(rs.getInt("ca.id"))) {
                        capitals.add(Loan.cache.get(rs.getInt("ca.id")));
                        continue;
                    } else {
                        Loan lo = new Loan(rs.getInt("ca.id"), rs.getDouble("balance"), rs.getDouble("interest_rate"),
                            rs.getDouble("initial_fee"), rs.getInt("client_id"), rs.getInt("destination_id"));
                        Loan.cache.put(rs.getInt("ca.id"), lo);
                        capitals.add(lo);
                        continue;
                    }
                } else if (rs.getInt("sz.id") > 0){
                    if (SafeZone.cache.containsKey(rs.getInt("ca.id"))) {
                        capitals.add(SafeZone.cache.get(rs.getInt("ca.id")));
                        continue;
                    } else {
                        SafeZone sz = new SafeZone(rs.getInt("ca.id"), rs.getDouble("balance"), rs.getDouble("interest_rate"),
                            rs.getInt("client_id"));
                        SafeZone.cache.put(rs.getInt("ca.id"), sz);
                        capitals.add(sz);
                        continue;
                    }
                } else if (rs.getInt("cu.id") > 0 ){
                    if (Current.cache.containsKey(rs.getInt("ca.id"))) {
                        capitals.add(Current.cache.get(rs.getInt("ca.id")));
                        continue;
                    } else {
                        Current lo = new Current(rs.getInt("ca.id"), rs.getDouble("balance"),
                            rs.getInt("client_id"));
                        Current.cache.put(rs.getInt("ca.id"), lo);
                        capitals.add(lo);
                        continue;
                    }
                }
                System.out.println("Return Null");
            }
        /* --- Debug ---
        if (row == 0) {
            System.out.println("⚠️ Nenhum registro encontrado para client_id=" + getId());}
        System.out.println("--- Fim do resultado ---\n");
        // --- Debug ---*/
        }
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public Contact getContact() throws SQLException {return Contact.fromId(contact_id);}
    public List<Capital> getCapitals() {return capitals;}

    // Setters
    public Client setUsername(String username) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE client SET username=? WHERE id=?", username, getId());
        this.username = username;
        return this;
    }
    
    public Client setPassword(String password) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE client SET password=? WHERE id=?", password, getId());
        this.password = password;
        return this;
    }
    
    public Client setContact(int contact_id) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE client SET contact_id=? WHERE id=?", contact_id, getId());
        this.contact_id = contact_id;
        return this;
    }
}
