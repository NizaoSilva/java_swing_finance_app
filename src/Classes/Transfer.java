package Classes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class Transfer extends Operation {
    protected static final Map<Integer, Transfer> cache = new HashMap<>();
    protected final int destination_id;
    
    protected Transfer(int id, double value, LocalDateTime datetime, int capital_id, int destination_id) {
        super(id, value, datetime, capital_id);
        this.destination_id = destination_id;
    }
    
    public static Transfer create(double value, int capital_id, int destination_id) throws SQLException {
        Capital capital = Capital.fromId(capital_id);
        //if (value > capital.getBalance()) throw new IllegalArgumentException("Valor inválido: deve ser menor ou igual ao saldo do capital!");
        Capital destination = Capital.fromId(destination_id);
        DataBase db = new DataBase();
        LocalDateTime now = LocalDateTime.now();
        Timestamp sqlTimestamp = Timestamp.valueOf(now);
        capital.setBalance(capital.getBalance() - value);
        destination.setBalance(destination.getBalance() + value);
        //db.executeUpdate("UPDATE capital SET balance = ? WHERE id = ?", capital.getBalance() - value, capital.getId());
        //db.executeUpdate("UPDATE capital SET balance = ? WHERE id = ?", destination.getBalance() + value, destination_id);
        int op_id = db.executeInsert("INSERT INTO operation (datetime, value, capital_id) VALUES (?, ?, ?)", sqlTimestamp,value, capital_id);
        db.executeUpdate("INSERT INTO transfer (operation_id, destination_id) VALUES (?, ?)",op_id, destination_id);
        Transfer t = new Transfer(op_id, value, now, capital_id, destination_id);
        cache.put(op_id, t);
        if (Current.cache.containsKey(capital_id) || SafeZone.cache.containsKey(capital_id) ||
            Loan.cache.containsKey(capital_id) ) {Capital.fromId(capital_id).getOperations().add(t);}
        return t;
    }
    
    public static Transfer fromId(int id) throws SQLException {
        return (Transfer) Operation.fromId(id);
    }
    
    public int getDestinationId() {return this.destination_id;}
    public Capital getCapital() throws SQLException {return Capital.fromId(getDestinationId());}
}