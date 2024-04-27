package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

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

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "AdminPages/AdminDashboard";
    }

    @GetMapping("/getallusers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllUsers(Model model){
        model.addAttribute("users", userDAO.listAllUsers());
        return "AdminPages/getallusers";
    }

    @GetMapping("/getallairports")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllAirports(Model model){
        model.addAttribute("airports", airportsDAO.listAllAirports());
        return "AdminPages/getallairports";
    }

    @GetMapping("/getallairlines")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllAirlines(Model model){
        model.addAttribute("airlines", airlinesDAO.listAllAirlines());
        return "AdminPages/getallairlines";
    }

    @GetMapping("/getallplanes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllPlanes(Model model){
        model.addAttribute("planes", planesDAO.listAllPlanes());
        return "AdminPages/getallplanes";
    }

    @GetMapping("/getallroutes")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllRoutes(Model model){
        model.addAttribute("routes", routesDAO.listAllRoutes());
        return "AdminPages/getallroutes";
    }

    @GetMapping("/getallsubscription")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String listAllSubscription(Model model){
        model.addAttribute("Subscription", SubscriptionDAO.listAllSubscription());
        return "AdminPages/getallsubscription";
    }

    @PostMapping("/deleteUser")
    @Transactional  // Ensure atomicity
    public String deleteUser(@RequestParam("username") String username, RedirectAttributes redirectAttributes) {
        // Check if user exists
        int userId = userDAO.findUserIdByUsername(username);
        if (userId == 0) {
            redirectAttributes.addFlashAttribute("error", "User not found.");
            return "redirect:/admin/getallusers";
        }
        // Delete Authorities -> Subscription -> User
        try {
            userDAO.deleteAuthorities(username);
            userDAO.deleteSubscriptions(userId);
            userDAO.deleteUser(username);

            redirectAttributes.addFlashAttribute("message", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user. Please try again.");
            return "redirect:/admin/getallusers";
        }
        return "redirect:/admin/getallusers";
    }
}
