package com.upgrade.challenge.reservationservice.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationDateEntity;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationEntity;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationStatusEntity;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationStatusEntity.ReservationStatusEnum;

import org.springframework.stereotype.Repository;

@Repository
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

        private static final String CAMPSITE_ID_COLUMN = "campsiteId";
        private static final String BASE_36_ID_COLUMN = "base36Id";
        private static final String RESERVATION_STATUS_COLUMN = "reservationStatus";

        private static final String BASE_36_ID_QUERY = "SELECT r FROM Reservation r WHERE r.base36Id = :base36Id and r.reservationStatus.reservationStatus = :reservationStatus";
        private static final String DATE_OVERLAP_QUERY = "SELECT r FROM Reservation r JOIN r.reservationDates rd WHERE rd.reservationDate in :reservationDates and r.reservationStatus.reservationStatus in ('PENDING', 'RESERVED') and LOWER(r.campsiteId) = LOWER(:campsiteId)";
        private static final String RESERVATION_DATE_QUERY = "SELECT r FROM Reservation r JOIN r.reservationDates rd WHERE rd.reservationDate BETWEEN :arrivalDate AND :departureDate and r.reservationStatus.reservationStatus in ('PENDING', 'RESERVED') and LOWER(r.campsiteId) = LOWER(:campsiteId)";

        @PersistenceContext
        EntityManager entityManager;

        @Override
        public CompletableFuture<List<ReservationEntity>> findAllReservationsBetweenDates(
                        @NotNull @Pattern(regexp = CommonRegex.UUID_REGEX, message = "Campsite id invalid.") String campsiteId,
                        @NotNull LocalDate arrivalDate, @NotNull LocalDate departureDate) {
                return CompletableFuture.completedFuture(entityManager.createQuery(RESERVATION_DATE_QUERY)
                                .setParameter("arrivalDate", Date.valueOf(arrivalDate))
                                .setParameter("departureDate", Date.valueOf(departureDate))
                                .setParameter(CAMPSITE_ID_COLUMN, campsiteId).getResultList());
        }

        @Override
        public CompletableFuture<ReservationEntity> findByBase36Id(
                        @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX, message = "Base 36 id invalid.") String base36Id) {
                List<ReservationEntity> reservationEntities = entityManager.createQuery(BASE_36_ID_QUERY)
                                .setParameter(BASE_36_ID_COLUMN, base36Id)
                                .setParameter(RESERVATION_STATUS_COLUMN, ReservationStatusEnum.RESERVED.name())
                                .getResultList();
                if (!reservationEntities.isEmpty()) {
                        return CompletableFuture.completedFuture(reservationEntities.get(0));
                }
                return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletableFuture<ReservationEntity> reserveCampsite(@NotNull @Valid ReservationEntity reservationEntity)
                        throws InterruptedException {
                List<ReservationEntity> reservationEntities = entityManager.createQuery(DATE_OVERLAP_QUERY)
                                .setParameter(CAMPSITE_ID_COLUMN, reservationEntity.getCampsiteId())
                                .setParameter("reservationDates", reservationEntity.getReservationDates().stream()
                                                .map(rd -> rd.getReservationDate()).collect(Collectors.toList()))
                                .getResultList();
                if (reservationEntities.isEmpty()) {
                        try {
                                reservationEntity.setReservationStatus(
                                                new ReservationStatusEntity(ReservationStatusEnum.PENDING.name()));
                                for (ReservationDateEntity rd : reservationEntity.getReservationDates()) {
                                        rd.setReservation(reservationEntity);
                                }
                                entityManager.persist(reservationEntity);
                                return CompletableFuture.completedFuture(reservationEntity);
                        } catch (Exception e) {
                        }
                }
                return CompletableFuture.failedFuture(
                                new EntityExistsException("Campsite reservation dates are already used."));
        }

        @Override
        public CompletableFuture<ReservationEntity> finalizeReservation(@NotNull @Valid ReservationEntity reservation) {
                List<ReservationEntity> foundEntities = null;
                String base36Id = null;
                do {
                        base36Id = ReservationEntity.generateBase36Id();
                        foundEntities = entityManager
                                        .createQuery("select r from Reservation r where r.base36Id = :base36Id")
                                        .setParameter(BASE_36_ID_COLUMN, base36Id).getResultList();
                } while (!foundEntities.isEmpty());

                ReservationEntity foundReservation = entityManager.find(ReservationEntity.class, reservation.getId(),
                                LockModeType.PESSIMISTIC_WRITE);
                if (foundReservation != null) {
                        if (foundReservation.getReservationStatus().getReservationStatus()
                                        .equals(ReservationStatusEnum.PENDING.name())) {
                                foundReservation.setUserId(reservation.getUserId());
                                foundReservation.setBase36Id(base36Id);
                                foundReservation.setReservationStatus(
                                                new ReservationStatusEntity(ReservationStatusEnum.RESERVED.name()));
                                return CompletableFuture.completedFuture(foundReservation);
                        }
                        return CompletableFuture.failedFuture(new EntityNotFoundException(
                                        "Reservations with id " + reservation.getId() + " has the wrong status."));
                }
                return CompletableFuture.failedFuture(new EntityNotFoundException(
                                "Reservation with id " + reservation.getId() + " can't be found."));
        }

        @Override
        public CompletableFuture<ReservationEntity> updateReservation(@NotNull @Valid ReservationEntity reservation) {
                List<ReservationEntity> reservationEntities = entityManager.createQuery(BASE_36_ID_QUERY)
                                .setParameter(BASE_36_ID_COLUMN, reservation.getBase36Id())
                                .setParameter(RESERVATION_STATUS_COLUMN, ReservationStatusEnum.RESERVED.name())
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
                if (!reservationEntities.isEmpty()) {
                        ReservationEntity foundReservation = reservationEntities.get(0);
                        reservationEntities = entityManager.createQuery(DATE_OVERLAP_QUERY)
                                        .setParameter(CAMPSITE_ID_COLUMN, foundReservation.getCampsiteId())
                                        .setParameter("reservationDates",
                                                        reservation.getReservationDates().stream()
                                                                        .map(rd -> rd.getReservationDate())
                                                                        .collect(Collectors.toList()))
                                        .getResultList();
                        if (reservationEntities.isEmpty()) {
                                for (ReservationDateEntity rd : foundReservation.getReservationDates()) {
                                        entityManager.remove(rd);
                                }
                                foundReservation.getReservationDates().clear();
                                for (ReservationDateEntity rd : reservation.getReservationDates()) {
                                        rd.setReservation(foundReservation);
                                        foundReservation.getReservationDates().add(rd);
                                }
                                return CompletableFuture.completedFuture(foundReservation);
                        }
                        return CompletableFuture.failedFuture(
                                        new EntityExistsException("Campsite reservation dates are already used."));
                }
                return CompletableFuture.failedFuture(new EntityNotFoundException(
                                "Reservation with id " + reservation.getId() + " can't be found."));
        }

        @Override
        public CompletableFuture<ReservationEntity> deleteReservation(
                        @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX, message = "Base 36 id invalid.") String base36Id) {
                List<ReservationEntity> reservationEntities = entityManager.createQuery(BASE_36_ID_QUERY)
                                .setParameter(BASE_36_ID_COLUMN, base36Id)
                                .setParameter(RESERVATION_STATUS_COLUMN, ReservationStatusEnum.RESERVED.name())
                                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
                if (!reservationEntities.isEmpty()) {
                        ReservationEntity foundReservation = reservationEntities.get(0);
                        foundReservation.setReservationStatus(
                                        new ReservationStatusEntity(ReservationStatusEnum.CANCELED.name()));
                        return CompletableFuture.completedFuture(foundReservation);
                }
                return CompletableFuture.failedFuture(
                                new EntityNotFoundException("Reservation with id " + base36Id + " can't be found."));
        }
}
