package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
        return "UserPages/UserDashboard"; // 返回用户仪表板视图
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
        // 获取当前认证的用户的用户名
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // 从数据库获取用户ID
        int userId = userDAO.findUserIdByUsername(username);

        // 调用UserDAO来保存订阅
        userDAO.saveSubscriptions(userId, selectedFlights);

        return "UserPages/SubscribeSuccess";  // 重定向回搜索页面或确认页面
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
}