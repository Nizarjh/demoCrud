package com.example.demo.reservations;

import org.springframework.stereotype.Component;

@Component
public class DemoReservationMapper {
    public Demo toDomain(DemoEntity demoEntity) {
        return new Demo(
                demoEntity.getId(),
                demoEntity.getUserId(),
                demoEntity.getRoomId(),
                demoEntity.getStartDate(),
                demoEntity.getEndDate(),
                demoEntity.getStatus());

    }
    public DemoEntity toEntity(Demo demo) {
        return new DemoEntity(
                demo.id(),
                demo.userId(),
                demo.roomId(),
                demo.startDate(),
                demo.endDate(),
                demo.status());

    }
}
 