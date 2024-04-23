package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.UserDAO;
import com.example.cs411_final_project.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
        return "main"; // 返回主页面
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 返回登录页面
    }

//    @GetMapping("/logout")
//    public String logoutPage() {
//        return "main"; // 返回登出页面
//    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());  // 添加一个空的User对象供表单绑定
        return "Register";  // 返回注册视图页面
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            int userId = userDAO.addUser(user); // 尝试添加用户并获取新用户的 ID
            model.addAttribute("userId", userId); // 将用户 ID 传递到模型
            model.addAttribute("userName", user.getUserName()); // 将用户名传递到模型
            return "RegisterSuccess"; // 返回注册成功页面
        } catch (RuntimeException e) {
            model.addAttribute("user", user); // 重新绑定用户信息，以便能再次填写表单
            model.addAttribute("usernameError", "User Name EXISTS, use this to login or change your user name");
            return "Register"; // 由于异常，返回注册页面
        }
    }

    @PostMapping("/login")
    public ModelAndView login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Authentication authentication) {
        // 验证逻辑在Spring Security配置中处理
        if (authentication == null || !authentication.isAuthenticated()) {
            ModelAndView errorModel = new ModelAndView("login");
            errorModel.addObject("error", "CHECK YOUR USERNAME OR PASSWORD");
            return errorModel; // 验证失败，返回错误信息
        }

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_USER")) {
            return new ModelAndView("redirect:/user/dashboard"); // 重定向到 UserController 的用户仪表板方法
        } else if (roles.contains("ROLE_ADMIN")) {
            return new ModelAndView("redirect:/admin/dashboard"); // 重定向到 AdminController 的管理员仪表板方法
        }

        ModelAndView errorModel = new ModelAndView("login");
        errorModel.addObject("error", "Invalid role");
        return errorModel; // 角色不正确时返回错误
    }

    @GetMapping("/checkLoginAndRedirect")
    public ModelAndView checkLoginAndRedirect() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains("ROLE_ADMIN")) {
                return new ModelAndView("redirect:/admin/dashboard"); // 管理员身份，重定向到管理员仪表板
            } else if (roles.contains("ROLE_USER")) {
                return new ModelAndView("redirect:/user/dashboard"); // 用户身份，重定向到用户仪表板
            }
        }
        // 用户未登录或无有效身份，重定向到登录页面
        return new ModelAndView("redirect:/login");
    }
}


