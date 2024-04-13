package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.entity.Airports;
import com.example.cs411_final_project.AirportsRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


import org.springframework.ui.Model;


@Controller
public class AirportsController {
    @PostConstruct
    public void init() {
        System.out.println("AirportsController init");
    }
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

//    @RequestMapping(value = "/getairports", method= RequestMethod.GET)
//    @ResponseBody
//    public List<Map<String,Object>> getUsers() {
//        String sql = "SELECT * FROM Airports";
//        return jdbcTemplate.queryForList(sql);
//    }
    @Autowired
    private AirportsRepository airportsRepository;
    @GetMapping("/Airports")
    public String getAllAirportsData(Model model){
        Iterable<Airports> airports = airportsRepository.findAll();
        model.addAttribute("airports", airports);
        return "AllAirportsData";
    }
}
