package com.example.demo;

import java.time.LocalDate;

public record Demo(
        Long id,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status

) {
}
