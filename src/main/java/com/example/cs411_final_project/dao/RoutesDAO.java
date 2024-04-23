package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class RoutesDAO {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public RoutesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Routes> listAllRoutes() {
        String sql = "SELECT * FROM Routes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Routes(
                rs.getString("flightNumber"),
                rs.getInt("AirlineId"),
                rs.getInt("departureAirportID"),
                rs.getInt("arrivalAirportID"),
                rs.getInt("stops")));
    }

}
