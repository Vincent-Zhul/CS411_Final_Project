package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.entity.User;
import com.example.cs411_final_project.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import javax.annotation.PostConstruct;

import javax.annotation.PostConstruct;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("/user")
@Controller
public class UserController {
    @PostConstruct
    public void init(){System.out.println("UserController init");}

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @RequestMapping(value = "/getusers", method= RequestMethod.GET)
//    @ResponseBody
//    public List<Map<String,Object>> getUsers() {
//        String sql = "SELECT * FROM User";
//        return jdbcTemplate.queryForList(sql);
//    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signing")
    public String newUser(Model model){
        model.addAttribute("user", new User());
        return "UserSignUpForm";
    }

    @PostMapping("/signing")
    public String addUser(@ModelAttribute User user, Model model){
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        userRepository.save(user);
        model.addAttribute("user", newUser);
        return "SignUpSuccessPage";
    }

}
