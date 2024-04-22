package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.UserDAO;
import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute User user, Model model) {
        int userId = userDAO.addUser(user); // Add user and get the new user's ID
        model.addAttribute("userId", userId); // Pass User ID to the model
        model.addAttribute("userName", user.getUserName()); // Pass User Name to the model
        return "registersuccess"; // Redirect to the success page
    }
}
