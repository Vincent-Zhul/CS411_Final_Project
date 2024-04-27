package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.UserDAO;
import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Set;

@Controller
public class WebController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/")
    public String mainPage() {
        return "main"; // main page
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login page
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());  // Add an empty User object for form binding
        return "Register/Register";  // Return to the registration page
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            int userId = userDAO.addUser(user); // Add users and obtain new user ID
            model.addAttribute("userId", userId); // deliver the user ID to the model
            model.addAttribute("userName", user.getUserName()); // deliver the user name to the model
            return "Register/RegisterSuccess"; // Return to the registration success page
        } catch (RuntimeException e) {
            model.addAttribute("user", user); // error, return the user object to the model
            model.addAttribute("usernameError", "User Name EXISTS, use this to login or change your user name");
            return "Register/Register"; // Return to the registration page
        }
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            ModelAndView errorModel = new ModelAndView("login");
            errorModel.addObject("error", "CHECK YOUR USERNAME OR PASSWORD");
            return errorModel; // Verification failed, return to the login page
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_USER")) {
            return new ModelAndView("redirect:/user/dashboard"); // Redirect to the user dashboard
        } else if (roles.contains("ROLE_ADMIN")) {
            return new ModelAndView("redirect:/admin/dashboard"); // Redirect to the admin dashboard
        }

        ModelAndView errorModel = new ModelAndView("login");
        errorModel.addObject("error", "Invalid role");
        return errorModel; // Return error when the authority is incorrect
    }

    @GetMapping("/checkLoginAndRedirect")
    public ModelAndView checkLoginAndRedirect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_ADMIN")) {
                return new ModelAndView("redirect:/admin/dashboard"); // admin authority
            } else if (roles.contains("ROLE_USER")) {
                return new ModelAndView("redirect:/user/dashboard"); // user authority
            }
        }
        // User not logged in or with no valid identity, redirected to login page
        return new ModelAndView("redirect:/login");
    }
}


