package com.example.demo.reservations;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class DemoService {

    private final DemoRepository repository;
    private final DemoReservationMapper mapper;
    public static final org.slf4j.Logger log = LoggerFactory.getLogger(DemoService.class);

    public DemoService(
            DemoRepository repository,
            DemoReservationMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public Demo getReservationByID(Long id) {
        DemoEntity demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id = " + id));

        return mapper.toDomain(demoEntity);
    }

    public List<Demo> getAllReservationByFilter(
            DemoSearchFilter filter) {
        int pageSize = filter.pageSize() != null
                ? filter.pageSize()
                : 15;
        int pageNumber = filter.pageNumber() != null
                ? filter.pageNumber()
                : 0;

        var pagable = Pageable
                .ofSize(pageSize)
                .withPage(pageNumber);
        List<DemoEntity> allEntities = repository.searchByFilter(
                filter.roomId(),
                filter.userId(),
                pagable);

        List<Demo> reservationList = allEntities.stream()
                .map(it -> mapper.toDomain(it))
                .toList();

        return reservationList;

    }

    public Demo createReservation(Demo resertocreate) {

        if (resertocreate.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }
        if (!resertocreate.endDate().isAfter(resertocreate.startDate())) {
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        var entityToSave = mapper.toEntity(resertocreate);
        entityToSave.setStatus(ReservationStatus.PENDING);
        return mapper.toDomain(repository.save(entityToSave));

    }

    public Demo updateReservation(Long id, Demo demoToupdate) {

        var demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id = " + id));

        if (demoEntity.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("Cannot modify reservation: status = " + demoEntity.getStatus());
        }
        if (!demoToupdate.endDate().isAfter(demoToupdate.startDate())) {
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }

        var reservationToSave = mapper.toEntity(demoToupdate);
        reservationToSave.setId(demoEntity.getId());
        reservationToSave.setStatus(ReservationStatus.PENDING);
        return mapper.toDomain(repository.save(reservationToSave));
    }

    @Transactional
    public void cancelReservation(Long id) {
        DemoEntity demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id = " + id));

        if (demoEntity.getStatus() == ReservationStatus.APPROVED) {
            throw new IllegalStateException("Cannot cancel approved reservation " + id);
        }
        if (demoEntity.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation " + id + " is already cancelled");
        }
        repository.setStatus(id, ReservationStatus.CANCELLED);
        log.info("Successfully cancel reservation: id = {}", id);

    }

    @Transactional
    @Scheduled(cron = "10 * * * * *")
    public void autoDelete() {
        repository.deleteAllCanceledEntity();
        log.info("Called autoDelete");
    }

    public Demo IsApproved(Long id) {

        var demoEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found by id = " + id));

        var isConflict = isReservationConflict(
                demoEntity.getRoomId(),
                demoEntity.getStartDate(),
                demoEntity.getEndDate());
        if (demoEntity.getStatus() != ReservationStatus.PENDING || isConflict) {
            throw new IllegalStateException(
                    "Cannot approve reservation: status = " + demoEntity.getStatus() + "id = " + id);
        }
        demoEntity.setStatus(ReservationStatus.APPROVED);
        repository.save(demoEntity);
        return mapper.toDomain(demoEntity);
    }

    private boolean isReservationConflict(
            Long roomid,
            LocalDate startDate,
            LocalDate endDate) {
        List<Long> conflictingIds = repository.findConflictReservationIds(roomid,
                startDate,
                endDate,
                ReservationStatus.APPROVED);
        if (conflictingIds.isEmpty()) {
            return false;
        }
        log.warn("Conflicting reservation IDs: {}", conflictingIds);

        return true;
    }

}