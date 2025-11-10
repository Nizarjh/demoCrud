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
        if (!demoMap.containsKey(id)) {
            throw new NoSuchElementException("Not found by id =" + id);
        }
        return demoMap.get(id);
    }

    public List<Demo> getReservationALL() {
        return new ArrayList<>(demoMap.values()) {
        };

    }

    public Demo createReservation(Demo resertocreate) {
        if (resertocreate.id() != null || resertocreate.status() != null) {
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

    public Demo updateReservation(Long id, Demo demoToupdate) {
        if (!demoMap.containsKey(id)) {
            throw new NoSuchElementException("Not found  id =" + id);
        }
        var reservation = demoMap.get(id);

        if (reservation.status() != ReservationStatus.PENDING) {
            throw new IllegalStateException("cannot modify reservation: status= " + reservation.status());
        }
        var updatedDemo = new Demo(
                reservation.id(),
                demoToupdate.userId(),
                demoToupdate.roomId(),
                demoToupdate.startDate(),
                demoToupdate.endDate(),
                ReservationStatus.PENDING);
        demoMap.put(reservation.id(), updatedDemo);
        demoMap.put(id, updatedDemo);
        return updatedDemo;
    }

    public void deleteReservation(Long id) {
        if (!demoMap.containsKey(id)) {
            throw new NoSuchElementException("Not found id =" + id);
        }
        demoMap.remove(id);
    }

    public Demo IsApproved(Long id) {
        if (!demoMap.containsKey(id)) {
            throw new NoSuchElementException("Not found id =" + id);
        }
        var reservation = demoMap.get(id);
        var isConflict = isReservationConflict(reservation);
        if (reservation.status() != ReservationStatus.PENDING || isConflict) {
            throw new IllegalStateException(
                    "Cannot approve reservation: status= " + reservation.status() + "id= " + id);
        }
        var approvedReservation = new Demo(
                reservation.id(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.APPROVED);
        demoMap.put(reservation.id(), approvedReservation);
        return approvedReservation;
    }

    private boolean isReservationConflict(Demo demoReservation) {
        for (Demo existingDemo : demoMap.values()) {
            if (demoReservation.id().equals(existingDemo.id())
                    || !demoReservation.roomId().equals(existingDemo.roomId())
                    || !existingDemo.status().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (demoReservation.startDate().isAfter(existingDemo.endDate())
                    && demoReservation.startDate().isBefore(demoReservation.endDate())) {
                return true;
            }
        }
        return false;
    }
}