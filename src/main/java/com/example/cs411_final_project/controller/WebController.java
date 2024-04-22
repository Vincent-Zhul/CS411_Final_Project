package com.example.cs411_final_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

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

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // return register page
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

