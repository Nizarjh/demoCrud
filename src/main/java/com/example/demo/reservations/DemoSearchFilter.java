package com.example.demo.reservations;

public record DemoSearchFilter(
        Long roomId,
        Long userId,
        Integer pageSize,
        Integer pageNumber) {

}
