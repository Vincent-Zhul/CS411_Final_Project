package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.Airports;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AirportsDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AirportsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Airports> listAllAirports() {
        String sql = "SELECT * FROM Airports";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Airports(
                rs.getInt("airportID"),
                rs.getString("airportName"),
                rs.getString("city"),
                rs.getString("country"),
                rs.getString("IATA"),
                rs.getString("ICAO"),
                rs.getString("TimeZone")
        ));
    }
}
