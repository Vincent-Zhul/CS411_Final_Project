package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.Planes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlanesDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlanesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Planes> listAllPlanes() {
        String sql = "SELECT * FROM Planes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Planes(
                rs.getString("planeName"),
                rs.getString("IATA"),
                rs.getString("ICAO")
        ));
    }

    public Planes getPlaneByName(String planeName) {
        String sql = "SELECT * FROM Planes WHERE planeName = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{planeName}, (rs, rowNum) -> new Planes(
                rs.getString("planeName"),
                rs.getString("IATA"),
                rs.getString("ICAO")
        ));
    }
}
