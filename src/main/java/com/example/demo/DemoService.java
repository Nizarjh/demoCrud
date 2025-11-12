package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DemoService {

    private final DemoRepository repository;

    public DemoService(DemoRepository repository) {

        this.repository = repository;
    }

    public Demo getReservationByID(Long id) {
        DemoEntity demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id =" + id));

        return toDomainDemo(demoEntity);
    }

    public List<Demo> getReservationALL() {
        List<DemoEntity> allEntities = repository.findAll();

        List<Demo> reservationList = allEntities.stream()
                .map(it -> toDomainDemo(it))
                .toList();

        return reservationList;

    }

    public Demo createReservation(Demo resertocreate) {
        if (resertocreate.id() != null || resertocreate.status() != null) {
            throw new IllegalArgumentException("Id and status should be empty");
        }

        var entityToSave = new DemoEntity(
                null,
                resertocreate.userId(),
                resertocreate.roomId(),
                resertocreate.startDate(),
                resertocreate.endDate(),
                ReservationStatus.PENDING);
        return toDomainDemo(repository.save(entityToSave));

    }

    public Demo updateReservation(Long id, Demo demoToupdate) {

        var demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id =" + id));

        if (demoEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("cannot modify reservation: status= " + demoEntity.getStatus());
        }
        var updatedDemo = new DemoEntity(
                demoEntity.getId(),
                demoToupdate.userId(),
                demoToupdate.roomId(),
                demoToupdate.startDate(),
                demoToupdate.endDate(),
                ReservationStatus.PENDING);

        return toDomainDemo(repository.save(updatedDemo));
    }

    public void deleteReservation(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Not found id =" + id);
        }
        repository.deleteById(id);
    }

    public Demo IsApproved(Long id) {

        var demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id =" + id));

        var isConflict = isReservationConflict(demoEntity);
        if (demoEntity.getStatus() != ReservationStatus.PENDING || isConflict) {
            throw new IllegalStateException(
                    "Cannot approve reservation: status= " + demoEntity.getStatus() + "id= " + id);
        }
        demoEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(demoEntity);
        return toDomainDemo(demoEntity);
    }

    private boolean isReservationConflict(DemoEntity demoReservation) {
        var allReservation = repository.findAll();
        for (DemoEntity existingDemoEntity : allReservation) {
            if (demoReservation.getId().equals(existingDemoEntity.getId())
                    || !demoReservation.getRoomId().equals(existingDemoEntity.getRoomId())
                    || !existingDemoEntity.getStatus().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (demoReservation.getStartDate().isAfter(existingDemoEntity.getEndDate())
                    && demoReservation.getStartDate().isBefore(demoReservation.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    private Demo toDomainDemo(DemoEntity demoEntity) {
        return new Demo(
                demoEntity.getId(),
                demoEntity.getUserId(),
                demoEntity.getRoomId(),
                demoEntity.getStartDate(),
                demoEntity.getEndDate(),
                demoEntity.getStatus());

    }

}