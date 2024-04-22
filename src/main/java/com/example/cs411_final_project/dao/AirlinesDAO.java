package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.Airlines;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AirlinesDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AirlinesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Airlines> listAllAirlines() {
        String sql = "SELECT * FROM Airlines";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Airlines(
                rs.getInt("airlineID"),
                rs.getString("airlineName"),
                rs.getString("Alias"),
                rs.getString("IATA"),
                rs.getString("ICAO"),
                rs.getString("Callsign"),
                rs.getString("Country"),
                rs.getString("Active").charAt(0)
        ));
    }

    public Airlines getAirlineById(int airlineID) {
        String sql = "SELECT * FROM Airlines WHERE airlineID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{airlineID}, (rs, rowNum) -> new Airlines(
                rs.getInt("airlineID"),
                rs.getString("airlineName"),
                rs.getString("Alias"),
                rs.getString("IATA"),
                rs.getString("ICAO"),
                rs.getString("Callsign"),
                rs.getString("Country"),
                rs.getString("Active").charAt(0)
        ));
    }
}
