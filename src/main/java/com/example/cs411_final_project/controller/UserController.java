package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.UserDAO;
import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private UserDAO userDAO;

//    @GetMapping("/getusers")
//    public String getUsers(Model model) throws SQLException {
//        List<User> users = userDAO.listAllUsers();
//        model.addAttribute("users", users);
//        return "AllUsersPage";
//    }

    @GetMapping("/register")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "UserSignUpForm";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute User user, Model model) {
        int userId = userDAO.addUser(user); // Add user and get the new user's ID
        model.addAttribute("userId", userId); // Pass User ID to the model
        model.addAttribute("userName", user.getUserName()); // Pass User Name to the model
        return "SignUpSuccessPage"; // Redirect to the success page
    }
}
