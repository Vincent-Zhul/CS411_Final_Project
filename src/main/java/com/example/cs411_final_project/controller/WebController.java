package com.example.cs411_final_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.cs411_final_project.entity.User;
import com.example.cs411_final_project.dao.UserDAO;

import javax.servlet.http.HttpSession;

@Controller
public class WebController {

    @GetMapping("/")
    public String mainPage() {
        return "main"; // return main page
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // return login page
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userDAO.authenticate(username, password);
        if (user != null) {
            session.setAttribute("userID", user.getUserID());
            return "redirect:/user/home";
        } else {
            return "login";
        }
    }

    @GetMapping("/register")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "register"; // return register page
    }

    @Autowired
    private UserDAO userDAO;

    @PostMapping("/register")
    public String addUser(@ModelAttribute User user, Model model) {
        int userId = userDAO.addUser(user); // Add user and get the new user's ID
        model.addAttribute("userId", userId); // Pass User ID to the model
        model.addAttribute("userName", user.getUserName()); // Pass User Name to the model
        return "registersuccess"; // Redirect to the success page
    }

    @RequestMapping("/userdashboard")
    public String userDashboard() {
        return "userdashboard"; // return user dashboard
    }

    @RequestMapping("/admindashboard")
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "admindashboard"; // 如果是管理员，返回管理员仪表板视图
        }
        return "accessdenied"; // 如果不是管理员，返回访问被拒绝页面
    }
}

