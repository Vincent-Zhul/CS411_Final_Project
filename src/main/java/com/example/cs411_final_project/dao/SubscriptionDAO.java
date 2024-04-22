package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

public class SubscriptionDAO {
    private final JdbcTemplate jdbcTemplate;

    public SubscriptionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    


}
