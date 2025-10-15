package com.example.demo;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class DemOService {

        public Demo getReservationByID(Long id){
        return new Demo(
            id,
            100L,
            40L,
            LocalDate.now(),
            LocalDate.now().plusDays(5),
            ReservationStatus.APPROVED
        );
    }
}