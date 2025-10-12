package Classes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Deposit extends Operation {
    protected static final Map<Integer, Deposit> cache = new HashMap<>();
    
    protected Deposit(int id, double value, LocalDateTime datetime, int capital_id) {
        super(id, value, datetime, capital_id);
    }

    public static Deposit create(double value, int capital_id) throws SQLException {
        Capital capital = Capital.fromId(capital_id);
        DataBase db = new DataBase();
        LocalDateTime now = LocalDateTime.now();
        Timestamp sqlTimestamp = Timestamp.valueOf(now);
        capital.setBalance(capital.getBalance() + value);
        //db.executeUpdate("UPDATE capital SET balance = ? WHERE id = ?", capital.getBalance() + value, capital.getId());
        int op_id = db.executeInsert("INSERT INTO operation (datetime, value, capital_id) VALUES (?, ?, ?)", sqlTimestamp,value, capital_id);
        db.executeUpdate("INSERT INTO deposit (operation_id) VALUES (?)",op_id);
        Deposit d = new Deposit(op_id, value, now, capital_id);
        cache.put(op_id, d);
        if (Current.cache.containsKey(capital_id) || SafeZone.cache.containsKey(capital_id) ||
            Loan.cache.containsKey(capital_id) ) {Capital.fromId(capital_id).getOperations().add(d);}
        return d;
    }
    
    public static Deposit fromId(int id) throws SQLException {
        return (Deposit) Operation.fromId(id);
    }
}