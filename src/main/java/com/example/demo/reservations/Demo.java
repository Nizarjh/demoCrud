package com.example.demo.reservations;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public record Demo(
        @Null
        Long id,
        @NotNull
        Long userId,
        @NotNull
        Long roomId,
        @NotNull
        @FutureOrPresent
        LocalDate startDate,
        @NotNull
        @FutureOrPresent
        LocalDate endDate,
        ReservationStatus status

) {
}
