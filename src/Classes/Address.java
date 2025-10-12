package Classes;

import java.sql.*;
import java.util.*;

public class Address {
    protected static final Map<Integer, Address> cache = new HashMap<>();
    private final int id;
    private String street, number, neighborhood, city, uf, country, zipCode;
    private final List<Contact> contacts = new ArrayList<>();

    // Construtor privado → só factory pode chamar
    private Address(int id, String street, String number, String neighborhood,
                    String city, String uf, String country, String zipCode) {
        this.id = id; this.street = street; this.number = number; this.neighborhood = neighborhood;
        this.city = city; this.uf = uf; this.country = country; this.zipCode = zipCode;
    }

    // Factory method (garante instância única)
    public static Address create(String street, String number, String neighborhood, String city,
            String uf, String country, String zipCode) throws SQLException {
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery(
                "SELECT id FROM address WHERE street=? AND number=? AND neighborhood=? AND city=? AND uf=? AND country=? AND zip_code=?",
                street, number, neighborhood, city, uf, country, zipCode)) {
            if (rs.next()) {
                int id = rs.getInt("id");
                if (cache.containsKey(id)) {
                    return cache.get(id);
                }
                Address existente = fromId(id);
                return existente;
            }
        }
        // Se não existe, cria
        int id = db.executeInsert(
                "INSERT INTO address (street, number, neighborhood, city, uf, country, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?)",
                street, number, neighborhood, city, uf, country, zipCode
        );
        Address a = new Address(id, street, number, neighborhood, city, uf, country, zipCode);
        cache.put(id, a);
        return a;
    }

    public static Address fromId(int id) throws SQLException {
        if (cache.containsKey(id)) return cache.get(id);
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM address WHERE id = ?", id)) {
            if (rs.next()) {
                Address a = new Address(rs.getInt("id"),rs.getString("street"),rs.getString("number"),
                        rs.getString("neighborhood"),rs.getString("city"),rs.getString("uf"),
                        rs.getString("country"),rs.getString("zip_code"));
                cache.put(id, a);
                return a;
            }
        }
        return null;
    }

    // Atualiza lista de contatos
    public void refreshContacts() throws SQLException {
        contacts.clear();
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM contact WHERE address_id = ?", getId())) {
            while (rs.next()) {
                if (Contact.cache.containsKey(rs.getInt("id"))) {
                    contacts.add(Contact.cache.get(rs.getInt("id")));
                } else {
                    Contact c = new Contact(rs.getInt("id"),rs.getString("name"),rs.getString("email"),
                            rs.getString("phone"), rs.getString("date_of_birth"), rs.getString("cpf"), getId());
                    Contact.cache.put(rs.getInt("id"), c);
                    contacts.add(c);
                }
            }
        }
    }

    // Getters
    public int getId() { return id; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getUf() { return uf; }
    public String getCountry() { return country; }
    public String getZipCode() { return zipCode; }
    public List<Contact> getContacts() { return contacts; }

    // Setters
    public Address setValues(String street, String number, String neighborhood,
                       String city, String uf, String country, String zipCode) throws SQLException {
        DataBase db = new DataBase();
        // Faz setValues
        db.executeUpdate(
                "UPDATE address SET street=?, number=?, neighborhood=?, city=?, uf=?, country=?, zip_code=? WHERE id=?",
                street, number, neighborhood, city, uf, country, zipCode, this.id
        );
        // Atualiza objeto em memória
        this.street = street; this.number = number; this.neighborhood = neighborhood; this.city = city; this.uf = uf; this.country = country; this.zipCode = zipCode;
        return this;
    }
}
