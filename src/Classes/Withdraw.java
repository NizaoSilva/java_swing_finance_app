package Classes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.JOptionPane;

public class Withdraw extends Operation {
    protected static final Map<Integer, Withdraw> cache = new HashMap<>();
    
    protected Withdraw(int id, double value, LocalDateTime datetime, int capital_id) {
        super(id, value, datetime, capital_id);
    }
    
    public static Withdraw create(double value, int capital_id) throws SQLException {
        Capital capital = Capital.fromId(capital_id);
       // if (value > capital.getBalance()) throw new IllegalArgumentException("Valor inválido: deve ser menor ou igual ao saldo do capital!");
        DataBase db = new DataBase();
        LocalDateTime now = LocalDateTime.now();
        Timestamp sqlTimestamp = Timestamp.valueOf(now);
        capital.setBalance(capital.getBalance() - value);
        //db.executeUpdate("UPDATE capital SET balance = ? WHERE id = ?", capital.getBalance() - value, capital_id);
        int op_id = db.executeInsert("INSERT INTO operation (datetime, value, capital_id) VALUES (?, ?, ?)", sqlTimestamp,value, capital_id);
        db.executeUpdate("INSERT INTO withdraw (operation_id) VALUES (?)",op_id);
        Withdraw w = new Withdraw(op_id, value, now, capital_id);
        cache.put(op_id, w);
        if (Current.cache.containsKey(capital_id) || SafeZone.cache.containsKey(capital_id) ||
            Loan.cache.containsKey(capital_id) ) {Capital.fromId(capital_id).getOperations().add(w);}
        return w;
    }
    
    public static Withdraw fromId(int id) throws SQLException {
        return (Withdraw) Operation.fromId(id);
    }
}