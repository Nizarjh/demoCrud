package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Democontroller {
    private final DemOService demOService;
    public Democontroller(DemOService demOService){
        this.demOService = demOService;
    }
    
}