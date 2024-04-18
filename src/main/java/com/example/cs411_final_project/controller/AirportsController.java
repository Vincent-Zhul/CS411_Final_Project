package com.example.cs411_final_project.controller;

import com.example.cs411_final_project.dao.AirportsDAO;
import com.example.cs411_final_project.entity.Airports;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


import org.springframework.ui.Model;

import java.util.List;


@Controller
public class AirportsController {

//    @Autowired
//    private AirportsDAO airportsDAO;
//
//    @GetMapping("/getairports")
//    public String getAirports(Model model) {
//        List<Airports> airports = airportsDAO.listAllAirports();
//        model.addAttribute("airports", airports);
//        return "AllAirportsPage";
//    }
}
