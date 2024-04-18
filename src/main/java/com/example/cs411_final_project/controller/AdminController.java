package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.AirportsDAO;
import com.example.cs411_final_project.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserDAO userDAO;
    private final AirportsDAO airportsDAO;

    public AdminController(UserDAO userDAO, AirportsDAO airportsDAO) {
        this.userDAO = userDAO;
        this.airportsDAO = airportsDAO;
    }

    @GetMapping("/adminlogin")
    public String adminLogin() {
        return "AdminLogin";
    }

    @GetMapping("/admindashboard")
    public String adminDashboard() {
        return "AdminDashboard";
    }

    @GetMapping("/getallusers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllUsers(Model model){
        model.addAttribute("users", userDAO.listAllUsers());
        return "AllUsersPage";
    }

    @GetMapping("/getallairports")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllAirports(Model model){
        model.addAttribute("airports", airportsDAO.listAllAirports());
        return "AllAirportsPage";
    }
}
