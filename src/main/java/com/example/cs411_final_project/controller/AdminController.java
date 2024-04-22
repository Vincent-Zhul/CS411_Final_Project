package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.AirlinesDAO;
import com.example.cs411_final_project.dao.AirportsDAO;
import com.example.cs411_final_project.dao.PlanesDAO;
import com.example.cs411_final_project.dao.UserDAO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserDAO userDAO;
    private final AirportsDAO airportsDAO;
    private final AirlinesDAO airlinesDAO;
    private final PlanesDAO planesDAO;


    public AdminController(UserDAO userDAO, AirportsDAO airportsDAO, AirlinesDAO airlinesDAO, PlanesDAO planesDAO) {
        this.userDAO = userDAO;
        this.airportsDAO = airportsDAO;
        this.airlinesDAO = airlinesDAO;
        this.planesDAO = planesDAO;
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
        return "getallusers";
    }

    @GetMapping("/getallairports")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllAirports(Model model){
        model.addAttribute("airports", airportsDAO.listAllAirports());
        return "getallairports";
    }

    @GetMapping("/getallairlines")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllAirlines(Model model){
        model.addAttribute("airlines", airlinesDAO.listAllAirlines());
        return "getallairlines";
    }
}
