package com.example.demo;

import java.util.List;

import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Democontroller {
    private final DemoService DemoService;
    public static final org.slf4j.Logger log = LoggerFactory.getLogger(Democontroller.class);

    public Democontroller(DemoService DemoService) {
        this.DemoService = DemoService;
    }

    @GetMapping("/{id}")
    public Demo getReservationByID(@PathVariable("id") Long id) {

        return DemoService.getReservationByID(id);
    }

    @GetMapping()
    public List<Demo> getReservationByID(){

        return DemoService.getReservationALL();
    }

    @PostMapping
    public Demo createReservation(@RequestBody Demo resertocreate){
        log.info("Called createReservation");
        return DemoService.createReservation(resertocreate);
    }


}
