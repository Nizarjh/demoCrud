package com.example.demo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class DemoService {
    private final Map<Long, Demo> demoMap;
    private final AtomicLong idCounter;

    public DemoService() {
        demoMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public Demo getReservationByID(Long id) {
        if (demoMap.containsKey(id)) {
            throw new NoSuchElementException("LOl" + id);
        }
        return demoMap.get(id);
    }

    public List<Demo> getReservationALL() {
        return new ArrayList<>(demoMap.values()) {
        };

    }

    public Demo createReservation(Demo resertocreate) {
        if (resertocreate.id() != null && resertocreate.status() != null) {
            throw new IllegalArgumentException("Id and status should be empty");
        }

        var newDemo = new Demo(
                idCounter.incrementAndGet(),
                resertocreate.userId(),
                resertocreate.roomId(),
                resertocreate.startDate(),
                resertocreate.endDate(),
                ReservationStatus.PENDING);
                demoMap.put(newDemo.id(), newDemo);
                return newDemo;
    }
}