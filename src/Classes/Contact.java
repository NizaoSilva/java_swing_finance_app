package Classes;

import java.sql.*;
import java.util.*;

public class Contact {
    protected static final Map<Integer, Contact> cache = new HashMap<>();
    private final int id;
    private String name, email, phone, date_of_birth, cpf;
    private int address_id; 
    private final List<Client> clients = new ArrayList<>(); 

    protected Contact(int id, String name, String email, String phone, String date_of_birth, String cpf, int address_id) {
        this.id = id; this.name = name; this.email = email; this.phone = phone; this.date_of_birth = date_of_birth; this.cpf = cpf; this.address_id = address_id;
    }

    public static Contact create(String name, String email, String phone, String dateOfBirth, String cpf, int address_id) throws SQLException {
        DataBase db = new DataBase();
        int id;
        if (dateOfBirth == "") {
            id = db.executeInsert("INSERT INTO contact (name, email, phone, date_of_birth, cpf, address_id) VALUES (?, ?, ?, ?, ?, ?)",
                    name, email, phone, dateOfBirth, cpf, address_id);
        } else {
            id = db.executeInsert("INSERT INTO contact (name, email, phone, cpf, address_id) VALUES (?, ?, ?, ?, ?)",
                    name, email, phone, cpf, address_id);
        }
        Contact c = new Contact(id, name, email, phone, dateOfBirth, cpf, address_id);
        cache.put(id, c); //##Address.fromId(address_id).getContacts().add(c);// Need to add 
        if (Address.cache.containsKey(address_id)) {Address.fromId(address_id).getContacts().add(c);}
        return c;
    }

    public static Contact fromId(int id) throws SQLException {
        if (cache.containsKey(id)) return cache.get(id);
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM contact WHERE id = ?", id)) {
            if (rs.next()) {
                Contact c = new Contact(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("phone"),
                        rs.getString("date_of_birth"),rs.getString("cpf"), rs.getInt("address_id"));
                cache.put(id, c);
                return c;
            }
        }
        return null;
    }

    // Recupera um contato pelo CPF
    public static Contact fromCpf(String cpf) throws SQLException {
        for (Contact c : cache.values()) {
            if (cpf.equals(c.getCpf())) {
                return c;
            }
        }
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM contact WHERE cpf = ?", cpf)) {
            if (rs.next()) {
                Contact c = new Contact(rs.getInt("id"),rs.getString("name"),rs.getString("email"),rs.getString("phone"),
                        rs.getString("date_of_birth"),rs.getString("cpf"), rs.getInt("address_id"));
                cache.put(c.getId(), c);
                return c;
            }
        }
        return null;
    }

    // Atualiza lista de clients
    public void refreshClients() throws SQLException {
        clients.clear();
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT c.*, client_id FROM client c LEFT JOIN admin a ON c.id = a.client_id WHERE contact_id = ?", getId())) {
            while (rs.next()) {
                if (rs.getInt("client_id") > 0) {
                    if (Admin.cache.containsKey(rs.getInt("id"))) {
                        clients.add(Admin.cache.get(rs.getInt("id")));
                    } else {
                        Admin a = new Admin(rs.getInt("id"), rs.getString("username"), rs.getString("password"), getId());
                        Admin.cache.put(rs.getInt("id"), a);
                        clients.add(a);
                    }
                } else {
                    if (Client.cache.containsKey(rs.getInt("id"))) {
                        clients.add(Client.cache.get(rs.getInt("id")));
                    } else {
                        Client c = new Client(rs.getInt("id"), rs.getString("username"), rs.getString("password"), getId());
                        Client.cache.put(rs.getInt("id"), c);
                        clients.add(c);
                    }
                }
            }
        }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDateOfBirth() { return date_of_birth; }
    public String getCpf() { return cpf; }
    public Address getAddress() throws SQLException { return Address.fromId(address_id); }
    public List<Client> getClients() { return clients; }

    // Setters
    public Contact setName(String name) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE contact SET name=? WHERE id=?", name, this.id);
        this.name = name;
        return this;
    }

    public Contact setEmail(String email) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE contact SET email=? WHERE id=?", email, this.id);
        this.email = email;
        return this;
    }

    public Contact setCpf(String cpf) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE contact SET cpf=? WHERE id=?", cpf, this.id);
        this.cpf = cpf;
        return this;
    }
    
    public Contact setPhone(String phone) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE contact SET phone=? WHERE id=?", phone, this.id);
        this.phone = phone;
        return this;
    }

    public Contact setDateOfBirth(String dateOfBirth) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE contact SET date_of_birth=? WHERE id=?", dateOfBirth, this.id);
        this.date_of_birth = dateOfBirth;
        return this;
    }

    public Contact setAddress(int address_id) throws SQLException {
        DataBase db = new DataBase();
        db.executeUpdate("UPDATE contact SET address_id=? WHERE id=?", address_id, this.id);
        this.address_id = address_id;
        return this;
    }
}