package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.entity.User;
import com.example.cs411_final_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.servlet.http.Cookie;

@Controller
public class WebController {
    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String main() {
        return "main";  // 返回主界面视图
    }

    @GetMapping("/login")
    public String login(){
        return "login";  // 返回登录界面视图
    }

//    @GetMapping("/login")
//    public ModelAndView login(HttpServletRequest request) {
//        ModelAndView modelAndView = new ModelAndView("login");
//        String message = (String) request.getSession().getAttribute("loginMessage");
//        modelAndView.addObject("message", message);
//        return modelAndView;
//    }
//
//    @PostMapping("/login")
//    public String performLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
//        User user = userService.findUserByUsername(username);
//        if (user == null) {
//            request.getSession().setAttribute("loginMessage", "USER NOT EXIST, PLEASE RE-ENTER USERNAME OR REGISTER");
//            return "redirect:/login";
//        }
//        if (!user.getPassword().equals(password)) {
//            request.getSession().setAttribute("loginMessage", "WRONG PASSWORD, PLEASE RE-ENTER PASSWORD");
//            return "redirect:/login";
//        }
//        return "redirect:/main";
//    }
//
//    @GetMapping("/login-as-admin")
//    public String loginAsAdmin(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//                return "redirect:/admin";
//            } else {
//                Cookie cookie = new Cookie("JSESSIONID", null);
//                cookie.setMaxAge(0);
//                cookie.setPath("/");
//                response.addCookie(cookie);
//                request.getSession().setAttribute("loginMessage", "NOT ADMINISTER, PLEASE USE THE ADMINISTER'S ACCOUNT TO LOGIN");
//                return "redirect:/admin-login";
//            }
//        }
//        return "redirect:/login";
//    }
//
//    @GetMapping("/admin-login")
//    public ModelAndView adminLogin(HttpServletRequest request) {
//        ModelAndView modelAndView = new ModelAndView("admin-login");
//        String message = (String) request.getSession().getAttribute("loginMessage");
//        modelAndView.addObject("message", message);
//        return modelAndView;
//    }

    @GetMapping("/signup")
    public String signUp() {
        return "redirect:/user/register";
    }

    @GetMapping("/admin")
    public String admin() {
        return "AdminDashboard";
    }
}
