package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SubscriptionDAO {
    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public SubscriptionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static List<Subscription> listAllSubscription() {
        String sql = "SELECT * FROM Subscription";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Subscription(
                rs.getInt("userID"),
                rs.getString("flightNumber")
        ));
    }

    public List<String> getFlightNumberByUserID(int userID) {
        String sql = "SELECT flightNumber FROM Subscription WHERE userID = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{userID}, (rs, rowNum) -> rs.getString("flightNumber"));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // 或根据实际情况处理异常
        }
    }

    public void createSubscription(Subscription subscription) {
        String sql = "INSERT INTO Subscription (userID, flightNumber) VALUES (?, ?)";
        jdbcTemplate.update(sql, subscription.getUserID(), subscription.getFlightNumber());
    }

    public List<Map<String, Object>> getFlightDetailsByFlightNumber(String flightNumber) {
        String sql = "SELECT " +
                "r.flightNumber, " +
                "a1.airportName AS DepartureAirport, " +
                "a2.airportName AS ArrivalAirport, " +
                "a1.city AS DepartureCity, " +
                "a2.city AS ArrivalCity, " +
                "a1.country AS DepartureCountry, " +
                "a2.country AS ArrivalCountry, " +
                "r.stops " +
                "FROM Routes r " +
                "JOIN Airports a1 ON r.departureAirportID = a1.airportID " +
                "JOIN Airports a2 ON r.arrivalAirportID = a2.airportID " +
                "WHERE r.flightNumber = ?";

        return jdbcTemplate.query(sql, new Object[]{flightNumber}, new RowMapper<Map<String, Object>>() {
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> results = new HashMap<>();
                results.put("FlightNumber", rs.getString("flightNumber"));
                results.put("DepartureAirport", rs.getString("DepartureAirport"));
                results.put("ArrivalAirport", rs.getString("ArrivalAirport"));
                results.put("DepartureCity", rs.getString("DepartureCity"));
                results.put("ArrivalCity", rs.getString("ArrivalCity"));
                results.put("DepartureCountry", rs.getString("DepartureCountry"));
                results.put("ArrivalCountry", rs.getString("ArrivalCountry"));
                results.put("Stops", rs.getInt("stops"));
                return results;
            }
        });
    }

}
