package com.example.cs411_final_project.dao;

import com.example.cs411_final_project.entity.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;

@Repository
public class UserDAO {
    private JdbcTemplate jdbcTemplate;
    private static Logger logger = LoggerFactory.getLogger(UserDAO.class);

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public User authenticate(String username, String password) {
        try {
            String sql = "SELECT * FROM User WHERE userName = ? AND password = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{username, password}, (rs, rowNum) -> new User(
                    rs.getInt("userID"),
                    rs.getString("userName"),
                    rs.getString("password"),
                    rs.getString("email")
            ));
        } catch (DataAccessException e) {
            logger.error("Authentication failed for user: " + username, e);
            return null; // 如果没有找到用户或查询失败，返回null
        }
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

    @Transactional
    public int addUser(User user) {
        final String sql = "INSERT INTO User (userName, password, email) VALUES (?, ?, ?)";
        final String authoritySql = "INSERT INTO Authorities (username, authority) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
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

            return keyHolder.getKey().intValue();  // Return the ID of the newly created user
        } catch (DataAccessException e) {
            // Handle the exception thrown by the trigger
            throw new RuntimeException("Insert failed, possibly due to duplicate username: " + e.getMessage());
        }
    }

    public int findUserIdByUsername(String username) {
        String sql = "SELECT userID FROM User WHERE userName = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
    }

    public List<Map<String, Object>> searchFlights(String departureCity, String arrivalCity, int maxStops) {
        return jdbcTemplate.query("{CALL SearchFlights(?, ?, ?)}",
                new Object[]{departureCity, arrivalCity, maxStops},
                new RowMapper<Map<String, Object>>() {
                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> results = new HashMap<>();
                        results.put("FlightNumber", rs.getString("flightNumber"));
                        results.put("DepartureAirport", rs.getString("DepartureAirport"));
                        results.put("ArrivalAirport", rs.getString("ArrivalAirport"));
                        results.put("DepartureCity", rs.getString("DepartureCity"));
                        results.put("ArrivalCity", rs.getString("ArrivalCity"));
                        results.put("DepartureCountry", rs.getString("DepartureCountry"));
                        results.put("ArrivalCountry", rs.getString("ArrivalCountry"));
                        return results;
                    }
                });
    }

    public List<Map<String, Object>> getPopularFlights(int limit) {
        return jdbcTemplate.query("{CALL GetPopularFlightRoutes(?)}",
                new Object[]{limit},
                new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> results = new HashMap<>();
                        results.put("FlightNumber", rs.getString("flightNumber"));
                        results.put("SubscriberCount", rs.getInt("SubscriberCount"));
                        results.put("DepartureAirport", rs.getString("DepartureAirport"));
                        results.put("ArrivalAirport", rs.getString("ArrivalAirport"));
                        return results;
                    }
                });
    }



    public void saveSubscriptions(int userId, List<String> flightNumbers) {
        String sql = "INSERT INTO Subscription (flightNumber, userID) VALUES (?, ?)";
        try {
            for (String flightNumber : flightNumbers) {
                // 验证flightNumber和userId是否有效
                if (isValidFlightNumber(flightNumber) && isValidUserId(userId)) {
                    jdbcTemplate.update(sql, flightNumber, userId);
                } else {
                    logger.error("Invalid flightNumber or userID: " + flightNumber + ", " + userId);
                }
            }
        } catch (DataAccessException e) {
            logger.error("Error inserting subscriptions for userID: " + userId + " with flightNumbers: " + flightNumbers, e);
            throw e; // 可选择抛出异常，或者处理异常确保系统稳定性
        }
    }

    private boolean isValidFlightNumber(String flightNumber) {
        String sql = "SELECT COUNT(*) FROM Routes WHERE flightNumber = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{flightNumber}, Integer.class);
        return count > 0;
    }

    private boolean isValidUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM User WHERE userID = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
        return count > 0;
    }
}
