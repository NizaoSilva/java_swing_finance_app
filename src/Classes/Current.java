package Classes;

import java.sql.*;
import java.util.*;

public class Current extends Capital {
    protected static final Map<Integer, Current> cache = new HashMap<>();

    protected Current(int id, double balance, int client_id) {
        super(id, balance, client_id);
    }

    public static Current create(int client_id) throws SQLException {
        DataBase db = new DataBase();
        int id = db.executeInsert(
            "INSERT INTO capital (client_id, balance) VALUES (?, ?)",
            client_id, 0.0);
        db.executeInsert(
            "INSERT INTO current (capital_id) VALUES (?)",
            id);
        Current c = new Current(id, 0.0, client_id);
        cache.put(id, c);
        if (Client.cache.containsKey(client_id) || Admin.cache.containsKey(client_id)) {Client.fromId(client_id).getCapitals().add(c);}
        return c;
    }

    public static Current fromId(int id) throws SQLException {
        return (Current) Capital.fromId(id);
    }
    
    @Override
    public double getBalance() throws SQLException {return this.balance;}
}