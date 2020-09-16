package com.upgrade.challenge.reservationservice.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationEntity;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepositoryCustom {

        @Async
        public CompletableFuture<List<ReservationEntity>> findAllReservationsBetweenDates(
                        @NotNull @Pattern(regexp = CommonRegex.UUID_REGEX, message = "Campsite id invalid.") String campsiteId,
                        @NotNull LocalDate arrivalDate, @NotNull LocalDate departureDate);

        @Async
        public CompletableFuture<ReservationEntity> findByBase36Id(
                        @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX, message = "Base 36 id invalid.") String base36Id);

        @Async
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public CompletableFuture<ReservationEntity> reserveCampsite(@NotNull @Valid ReservationEntity reservationEntity)
                        throws InterruptedException;

        @Async
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public CompletableFuture<ReservationEntity> finalizeReservation(@NotNull @Valid ReservationEntity reservation);

        @Async
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public CompletableFuture<ReservationEntity> updateReservation(@NotNull @Valid ReservationEntity reservation);

        @Async
        @Transactional(propagation = Propagation.REQUIRES_NEW)
        public CompletableFuture<ReservationEntity> deleteReservation(
                        @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX, message = "Base 36 id invalid.") String base36Id);

}
