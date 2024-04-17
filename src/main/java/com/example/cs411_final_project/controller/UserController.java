package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    @PostConstruct
    public void init(){
        System.out.println("UserController init");
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserID(rs.getInt("userID"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    };

    @GetMapping("/getusers")
    public String getUsers(Model model) {
        String sql = "SELECT * FROM User";
        List<User> users = jdbcTemplate.query(sql, userRowMapper);
        System.out.println("Number of users fetched: " + users.size());
        model.addAttribute("users", users);
        return "AllUsersPage";
    }

    @GetMapping("/signing")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "UserSignUpForm";
    }

    @PostMapping("/signing")
    public String addUser(@ModelAttribute User user, Model model){
        final String sql = "INSERT INTO User (userName, password, email) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "userID" });
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            return ps;
        }, keyHolder);

        // 检索自动生成的主键
        if (keyHolder.getKey() != null) {
            user.setUserID(keyHolder.getKey().intValue());
        }

        model.addAttribute("user", user);
        return "SignUpSuccessPage";
    }
}
