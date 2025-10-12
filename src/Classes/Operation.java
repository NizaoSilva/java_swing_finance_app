package Classes;
import java.sql.*;
import java.time.LocalDateTime;

public abstract class Operation {
    protected final int id;
    protected double value;
    protected LocalDateTime datetime;
    protected final int capital_id;
    
    protected Operation(int id, double value, LocalDateTime datetime, int capital_id) {
        this.id = id; this.value = value; this.datetime = datetime; this.capital_id = capital_id;
    }
    
    protected static Operation fromId(int id) throws SQLException {
        if (Deposit.cache.containsKey(id)) return Deposit.cache.get(id);
        if (Withdraw.cache.containsKey(id)) return Withdraw.cache.get(id);
        if (Transfer.cache.containsKey(id)) return Transfer.cache.get(id);
        
        DataBase db = new DataBase();
        try (ResultSet rs = db.executeQuery("SELECT * FROM operation o LEFT JOIN deposit d ON o.id = d.operation_id "
                + "LEFT JOIN withdraw w ON o.id = w.operation_id LEFT JOIN transfer t ON o.id = t.operation_id WHERE o.id = ?", id)) {
            if (rs.next()) {
                if (rs.getInt("d.id") > 0) {
                    Deposit d = new Deposit(id, rs.getDouble("value"), rs.getTimestamp("datetime").toLocalDateTime() ,
                            rs.getInt("capital_id"));
                    Deposit.cache.put(id, d);
                    return d;
                } else if (rs.getInt("w.id") > 0) {
                    Withdraw w = new Withdraw(id, rs.getDouble("value"), rs.getTimestamp("datetime").toLocalDateTime() ,
                            rs.getInt("capital_id"));
                    Withdraw.cache.put(id, w);
                    return w;
                } else if (rs.getInt("t.id") > 0) {
                    Transfer t = new Transfer(id, rs.getDouble("value"), rs.getTimestamp("datetime").toLocalDateTime(),
                            rs.getInt("capital_id"), rs.getInt("destination_id"));
                    Transfer.cache.put(id, t);
                }
            }
        }
        return null;
    }

    public int getId() {return id;}
    public double getValue() {return value;}
    public LocalDateTime getDatetime() {return datetime;}
    public Capital getCapital() throws SQLException {return Capital.fromId(getId());}
}
