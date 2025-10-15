package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Democontroller {
    private final DemOService demOService;
    
    public Democontroller(DemOService demOService){
        this.demOService = demOService; 
    }
    @GetMapping("/{id}")
    public Demo getReservationByID(@PathVariable("id") Long id){
        return demOService.getReservationByID(id);
    }
}