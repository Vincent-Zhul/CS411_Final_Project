package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @GetMapping("/dashboard")
    public String userDashboard() {
        return "UserPages/UserDashboard"; // Return to user dashboard
    }

    @GetMapping("/search")
    public String showSearchForm(Model model) {
        return "UserPages/SearchFlights";
    }

    @PostMapping("/search")
    public String searchFlight(
            @RequestParam("departureCity") String departureCity,
            @RequestParam("arrivalCity") String arrivalCity,
            @RequestParam("maxStops") int maxStops,
            Model model) {
        List<Map<String, Object>> flights = userDAO.searchFlights(departureCity, arrivalCity, maxStops);
        model.addAttribute("flights", flights);
        return "UserPages/SearchFlights";
    }

    @GetMapping("/popular")
    public String showPopularFlights(Model model) {
        return "UserPages/PopularFlights";
    }

    @PostMapping("/popular")
    public String searchFlight(
            @RequestParam(name = "limitNumber", required = false, defaultValue = "10") int limit,
            Model model) {
        List<Map<String, Object>> flights = userDAO.getPopularFlights(limit);
        model.addAttribute("flights", flights);
        return "UserPages/PopularFlights";
    }

    @PostMapping("/subscribe")
    public String subscribeFlights(
            @RequestParam("selectedFlights") List<String> selectedFlights,
            RedirectAttributes redirectAttributes) {
        // Obtain the username of the currently authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Get User ID
        int userId = userDAO.findUserIdByUsername(username);

        // Save subscriptions
        userDAO.saveSubscriptions(userId, selectedFlights);

        return "UserPages/SubscribeSuccess";  // Redirect back to the search page or confirmation page
    }

    @GetMapping("/subscription")
    public String viewUserSubscriptions(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        int userId = userDAO.findUserIdByUsername(username);  // Fetch user ID by username

        // Fetch all flight numbers associated with the user's subscriptions
        List<String> flightNumbers = subscriptionDAO.getFlightNumberByUserID(userId);

        // Fetch detailed flight information for each flight number
        List<Map<String, Object>> flightDetailsList = new ArrayList<>();
        for (String flightNumber : flightNumbers) {
            List<Map<String, Object>> flightDetails = subscriptionDAO.getFlightDetailsByFlightNumber(flightNumber);
            flightDetailsList.addAll(flightDetails); // Assumes multiple details could be returned, handle accordingly
        }

        model.addAttribute("flightDetails", flightDetailsList);  // Add detailed flight information to model

        return "UserPages/UserSubscription";  // Return the name of the Thymeleaf template
    }

    @PostMapping("/deleteSubscription")
    public String deleteSubscription(
            @RequestParam("flightNumber") String flightNumber,
            RedirectAttributes redirectAttributes) {
        // Get current user's ID
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        int userId = userDAO.findUserIdByUsername(username);

        // Delete the subscription
        subscriptionDAO.deleteSubscription(userId, flightNumber);

        // Redirect with a success message
        redirectAttributes.addFlashAttribute("message", "Subscription deleted successfully.");

        return "redirect:/user/subscription";  // Redirect to the subscription view
    }
    // Method for user self-deletion
    @PostMapping("/deleteAccount")
    @Transactional
    public String deleteAccount(RedirectAttributes redirectAttributes) {
        // Get the current authenticated user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ?
                ((UserDetails) principal).getUsername() : principal.toString();

        // Delete in the order: Authorities -> Subscription -> User
        userDAO.deleteAuthorities(username);
        int userId = userDAO.findUserIdByUsername(username);
        userDAO.deleteSubscriptions(userId);
        userDAO.deleteUser(username);

        redirectAttributes.addFlashAttribute("message", "Account deleted successfully.");
        return "redirect:/logout";
    }
}