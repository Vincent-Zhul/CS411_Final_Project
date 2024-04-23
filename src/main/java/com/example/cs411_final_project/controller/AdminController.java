package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.*;
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
    private final RoutesDAO routesDAO;
    private final SubscriptionDAO subscriptionDAO;


    public AdminController(UserDAO userDAO, AirportsDAO airportsDAO, AirlinesDAO airlinesDAO, PlanesDAO planesDAO, RoutesDAO routesDAO, SubscriptionDAO subscriptionDAO) {
        this.userDAO = userDAO;
        this.airportsDAO = airportsDAO;
        this.airlinesDAO = airlinesDAO;
        this.planesDAO = planesDAO;
        this.routesDAO = routesDAO;
        this.subscriptionDAO = subscriptionDAO;
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

    @GetMapping("/getallplanes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllPlanes(Model model){
        model.addAttribute("Planes", planesDAO.listAllPlanes());
        return "getallplanes";
    }

    @GetMapping("/getallroutes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllRoutes(Model model){
        model.addAttribute("Routes", routesDAO.listAllRoutes());
        return "getallroutes";
    }

    @GetMapping("/getallsubscription")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllSubscription(Model model){
        model.addAttribute("Subscription", SubscriptionDAO.listAllSubscription());
        return "getallsubscription";
    }
}
