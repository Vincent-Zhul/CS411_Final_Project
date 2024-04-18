package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> listAllUsers() {
        String sql = "SELECT * FROM User";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getInt("userID"),
                rs.getString("userName"),
                rs.getString("password"),
                rs.getString("email")
        ));
    }

    public int addUser(User user) {
        final String sql = "INSERT INTO User (userName, password, email) VALUES (?, ?, ?)";
        final String authoritySql = "INSERT INTO Authorities (username, authority) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"userID"});
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());  // Store the password in plain text
            ps.setString(3, user.getEmail());
            return ps;
        }, keyHolder);

        // Now insert into Authorities table using the same JdbcTemplate
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(authoritySql);
            ps.setString(1, user.getUserName());
            ps.setString(2, "ROLE_USER");  // Set default authority to ROLE_USER
            return ps;
        });

        return keyHolder.getKey().intValue(); // Return the ID of the newly created user
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT * FROM User WHERE userName = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> new User(
                rs.getInt("userID"),
                rs.getString("userName"),
                rs.getString("password"),
                rs.getString("email")
        ));
    }
}
