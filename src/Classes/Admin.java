package Classes;

import java.sql.*;
import java.util.*;

public class Admin extends Client {
    protected static final Map<Integer, Admin> cache = new HashMap<>();

    protected Admin(int id, String username, String password, int contact_id) {
        super(id, username, password, contact_id);
    }

    public static Admin create(String username, String password, int contact_id) throws SQLException {
        DataBase db = new DataBase();
        int id = db.executeInsert( "INSERT INTO client (contact_id, username, password) VALUES (?, ?, ?)",
            contact_id, username, password);
        db.executeUpdate("INSERT INTO admin (client_id) VALUES (?)", id);
        Admin a = new Admin(id, username, password, contact_id);
        cache.put(id, a);
        if (Contact.cache.containsKey(contact_id)) {Contact.fromId(contact_id).getClients().add(a);}
        return a;
    }

    public static Admin fromId(int id) throws SQLException {
        if (cache.containsKey(id)) return cache.get(id);
        /*DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT c.*, client_id FROM client c LEFT JOIN admin a ON c.id = client_id WHERE id = ?", id)) {
            if (rs.next()) {
                if (rs.getString("client_id").equals(rs.getString("id"))) {
                    Admin a = new Admin(id, rs.getString("username"), rs.getString("password"), 
                            Contact.fromId(rs.getInt("contact_id")));
                    Admin.cache.put(id, a);
                    return a;
                }
            }
        }
        return null;*/
        return (Admin) Client.fromId(id);
    }
}