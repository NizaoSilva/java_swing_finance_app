package Classes;
import java.sql.*;
import java.util.*;

public class Loan extends SafeZone {
    protected static final Map<Integer, Loan> cache = new HashMap<>();
    private final double initial_fee;
    private final int destination_id;

    protected Loan(int id, double balance, double interest_rate, double initial_fee, int client_id, int destination_id) {
        super(id, balance, interest_rate, client_id);
        this.initial_fee = initial_fee;
        this.destination_id = destination_id;
    }

    public static Loan create(double interest_rate, double value, double initial_fee, int client_id, int destination_id) throws SQLException {
        DataBase db = new DataBase();
        int id = db.executeInsert(
            "INSERT INTO capital (client_id, balance) VALUES (?, ?)",
            client_id, -value * initial_fee);
        int cu_id = db.executeInsert(
            "INSERT INTO current (capital_id) VALUES (?)",
            id);
        int sz_id = db.executeInsert(
            "INSERT INTO safezone (current_id, interest_rate) VALUES (?, ?)",
            cu_id, interest_rate);
        db.executeInsert(
            "INSERT INTO loan (safezone_id, initial_fee) VALUES (?, ?)",
            sz_id, initial_fee);
        
        Loan lo = new Loan(id, -value * initial_fee, interest_rate, initial_fee, client_id, destination_id);
        cache.put(id, lo);
        Transfer.create(value, id, destination_id);
        if (Client.cache.containsKey(client_id) || Admin.cache.containsKey(client_id)) {Client.fromId(client_id).getCapitals().add(lo);}
        return lo;
    }

    
    public static Loan fromId(int id) throws SQLException {
        return (Loan) Capital.fromId(id);
    }
    
    public double getInitialFee() {return this.initial_fee;}
    public Capital getDestination() throws SQLException {return Capital.fromId(this.destination_id);}
}