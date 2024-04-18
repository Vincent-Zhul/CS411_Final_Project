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

//    @GetMapping("/login-as-admin")
//    public String loginAsAdmin(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            // 检查是否是管理员
//            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//                return "redirect:/admin-dashboard";  // 管理员跳转到管理界面
//            } else {
//                // 清除Cookie，需要重新登录
//                Cookie cookie = new Cookie("auth_cookie", null);
//                cookie.setMaxAge(0);
//                cookie.setPath("/");
//                response.addCookie(cookie);
//
//                request.getSession().setAttribute("loginMessage", "NOT ADMINISTER, PLEASE USE THE ADMINISTER'S ACCOUNT TO LOGIN");
//                return "redirect:/admin-login";  // 重定向到管理员登录界面
//            }
//        }
//        return "redirect:/login";  // 未登录或认证失败，重定向到普通登录页面
//    }

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
