package Classes;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SafeZone extends Current {
    protected static final Map<Integer, SafeZone> cache = new HashMap<>();
    protected final double interest_rate;

    protected SafeZone(int id, double balance, double interest_rate, int client_id) {
        super(id, balance, client_id);
        this.interest_rate = interest_rate;
    }

    public static SafeZone create(double interest_rate, int client_id) throws SQLException {
        DataBase db = new DataBase();
        int id = db.executeInsert(
            "INSERT INTO capital (client_id, balance) VALUES (?, ?)",
            client_id, 0.0);
        int cu_id = db.executeInsert(
            "INSERT INTO current (capital_id) VALUES (?)",
            id);
        db.executeInsert(
            "INSERT INTO safezone (current_id, interest_rate) VALUES (?, ?)",
            cu_id, interest_rate);
        SafeZone sz = new SafeZone(id, 0.0,interest_rate, client_id);
        cache.put(id, sz);
        if (Client.cache.containsKey(client_id) || Admin.cache.containsKey(client_id)) {Client.fromId(client_id).getCapitals().add(sz);}
        return sz;}
    
    
    public static SafeZone fromId(int id) throws SQLException {
        return (SafeZone) Capital.fromId(id);
    }

    @Override
    public double getBalance() throws SQLException {
        DataBase db = new DataBase();
        LocalDate hoje = LocalDate.now();
        double saldoAtualizado = this.balance;
        ResultSet rs = db.executeQuery(
                "SELECT datetime FROM operation o LEFT JOIN transfer t ON o.id = t.operation_id " + 
                "WHERE capital_id = ? OR destination_id = ? ORDER BY datetime DESC LIMIT 1",getId(),getId());
        if (rs.next()) {
            String datetimeStr = rs.getString("datetime");
            LocalDateTime ultimaDataHora = java.time.LocalDateTime.parse(datetimeStr, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDate ultimaData = ultimaDataHora.toLocalDate(); 
            long dias = java.time.temporal.ChronoUnit.DAYS.between(ultimaData, hoje);
            saldoAtualizado = this.balance * Math.pow((1 + this.interest_rate), dias);
        }
        return saldoAtualizado;
    }
    
    public double getInterestRate() {return this.interest_rate;}
}