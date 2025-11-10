package com.example.demo;

import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/Reservation")
@RestController
public class Democontroller {
    private final DemoService DemoService;
    public static final org.slf4j.Logger log = LoggerFactory.getLogger(Democontroller.class);

    public Democontroller(DemoService DemoService) {
        this.DemoService = DemoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demo> getReservationByID(@PathVariable("id") Long id) {
        log.info("Called getReservationByID");
        return ResponseEntity.status(HttpStatus.OK)
                .body(DemoService.getReservationByID(id));
    }

    @GetMapping()
    public List<Demo> getReservationALL() {
        log.info("Called getReservationALL");
        return DemoService.getReservationALL();
    }

    @PostMapping
    public ResponseEntity<Demo> createReservation(@RequestBody Demo resertocreate) {
        log.info("Called createReservation");
        return ResponseEntity.status(201).body(DemoService.createReservation(resertocreate));

    }

    @PutMapping("/{id}")
    public ResponseEntity<Demo> updateReservation(
            @PathVariable("id") Long id,
            @RequestBody Demo demoToupdate) {
        log.info("Called updateReservation id={}, demoToUpdate={}", id, demoToupdate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(DemoService.updateReservation(id,demoToupdate));
    }

    @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteReservation(
            @PathVariable("id") Long id) {
        log.info("Called DeleteReservation id={}", id);
        try {
           DemoService.deleteReservation(id);
        return ResponseEntity.status(HttpStatus.OK).build(); 
        } catch (Exception e) {
            return ResponseEntity.status(404).build(); 
        }

    }
    @PostMapping("/{id}/approve")
    public ResponseEntity<Demo> IsApproved( @PathVariable("id") Long id){
        log.info("Called IsApproved id={}", id);
            return ResponseEntity.status(HttpStatus.OK)
                .body(DemoService.IsApproved(id));
    }
}
