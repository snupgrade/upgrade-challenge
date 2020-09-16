package com.upgrade.challenge.reservationservice.handler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;
import com.upgrade.challenge.commonservicelibrary.service.model.CampsiteModel;
import com.upgrade.challenge.commonservicelibrary.service.model.ReservationModel;
import com.upgrade.challenge.commonservicelibrary.service.model.UserModel;
import com.upgrade.challenge.commonservicelibrary.service.CampsiteService;
import com.upgrade.challenge.commonservicelibrary.service.UserService;
import com.upgrade.challenge.reservationservice.repository.ReservationRepository;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationDateEntity;
import com.upgrade.challenge.reservationservice.repository.entity.ReservationEntity;
import com.upgrade.challenge.reservationservice.util.DateUtils;
import com.upgrade.challenge.reservationservice.validator.ReservationValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ReservationHandler {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CampsiteService campsiteService;

    @Autowired
    private ReservationValidator reservationValidator;

    @Async
    public CompletableFuture<ReservationModel> getReservation(
            @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX) String base36Id) throws Throwable {
        CompletableFuture<ReservationEntity> reservationFuture = reservationRepository.findByBase36Id(base36Id);
        ReservationEntity reservation = reservationFuture.get();
        if (reservation == null) {
            return CompletableFuture
                    .failedFuture(new EntityNotFoundException("No reservation found with base_36_id : " + base36Id));
        }

        CompletableFuture<UserModel> userModelFuture = userService.getUserById(reservation.getUserId());
        UserModel userModel = userModelFuture.get();
        if (userModel == null) {
            return CompletableFuture.failedFuture(
                    new EntityNotFoundException("No user found for reservation with base_36_id : " + base36Id));
        }

        ReservationModel reservationModel = new ReservationModel(reservation.getId().toString(),
                reservation.getBase36Id(), reservation.getArrivaleDate(), reservation.getDepartureDate());
        reservationModel.setUser(userModel);
        return CompletableFuture.completedFuture(reservationModel);
    }

    @Async
    public CompletableFuture<List<LocalDate>> getReservationDates(
            @NotNull @Pattern(regexp = CommonRegex.UUID_REGEX) String campsiteId, LocalDate arrivalDate,
            LocalDate departureDate) throws Throwable {

        arrivalDate = arrivalDate == null ? LocalDate.now().plusDays(1) : arrivalDate;
        departureDate = departureDate == null ? arrivalDate.plusMonths(1) : arrivalDate;
        if (arrivalDate.isAfter(departureDate)) {
            return CompletableFuture
                    .failedFuture(new IllegalArgumentException("Arrival date can't be after departureDate"));
        }

        CompletableFuture<List<ReservationEntity>> reservationEntitiesFuture = reservationRepository
                .findAllReservationsBetweenDates(campsiteId, arrivalDate, departureDate);

        List<LocalDate> reservedDates = new ArrayList<>();
        reservationEntitiesFuture.get().stream().forEach(re -> {
            for (ReservationDateEntity reservationDateEntity : re.getReservationDates()) {
                reservedDates.add(reservationDateEntity.getReservationDate().toLocalDate());
            }
        });
        return CompletableFuture
                .completedFuture(DateUtils.findAvailableDatesBetweenRange(arrivalDate, departureDate, reservedDates));
    }

    @Async
    public CompletableFuture<ReservationModel> createReservation(
            @NotNull @Pattern(regexp = CommonRegex.UUID_REGEX) String campsiteId,
            @NotNull @Valid ReservationModel reservationModel) throws Throwable {

        if (!reservationValidator.validateReservationDates(reservationModel.getArrivalDate(),
                reservationModel.getDepartureDate())) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Date range is invalid."));
        }

        ReservationEntity reservationEntity = new ReservationEntity(campsiteId);
        reservationEntity.setReservationDates(DateUtils.createReservationDateEntityList(
                reservationModel.getArrivalDate(), reservationModel.getDepartureDate()));

        CompletableFuture<ReservationEntity> reservedReservationEntityFuture = reservationRepository
                .reserveCampsite(reservationEntity);
        CompletableFuture<UserModel> userModelFuture = userService.getUserByEmail(reservationModel.getUser().getEmail())
                .thenCompose(userModel -> {
                    if (userModel == null || userModel.isEmpty()) {
                        return userService.createUser(reservationModel.getUser());
                    }
                    return CompletableFuture.completedFuture(userModel.get(0));
                });
        CompletableFuture<CampsiteModel> campsiteModelFuture = campsiteService.getCampsiteById(campsiteId);

        ReservationEntity reservedReservationEntity = null;
        try {
            reservedReservationEntity = reservedReservationEntityFuture.get();
            CompletableFuture.allOf(userModelFuture, campsiteModelFuture).join();
            UserModel userModel = userModelFuture.get();
            CampsiteModel campsiteModel = campsiteModelFuture.get();

            if (campsiteModel != null) {
                if (userModel != null) {
                    reservedReservationEntity.setUserId(userModel.getUserId());
                    CompletableFuture<ReservationEntity> finalisedReservationEntityFuture = reservationRepository
                            .finalizeReservation(reservedReservationEntity);
                    ReservationEntity finalisedReservationEntity = finalisedReservationEntityFuture.get();

                    ReservationModel returnReservationModel = new ReservationModel(
                            finalisedReservationEntity.getId().toString(), finalisedReservationEntity.getBase36Id(),
                            finalisedReservationEntity.getArrivaleDate(),
                            finalisedReservationEntity.getDepartureDate());
                    returnReservationModel.setUser(userModel);
                    return CompletableFuture.completedFuture(returnReservationModel);
                }
                throw new Exception("Error while creating user.");
            }
            throw new Exception("Error while looking for campsite.");
        } catch (Exception e) {
            if (reservedReservationEntity != null) {
                reservationRepository.deleteById(reservedReservationEntity.getId());
            }
            throw e;
        }
    }

    @Async
    public CompletableFuture<ReservationModel> updateReservation(
            @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX) String base36Id,
            @NotNull @Valid ReservationModel reservationModel) throws Throwable {

        if (!reservationValidator.validateReservationDates(reservationModel.getArrivalDate(),
                reservationModel.getDepartureDate())) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Date range is invalid."));
        }

        ReservationEntity reservationEntity = new ReservationEntity(null, base36Id);
        reservationEntity.setReservationDates(DateUtils.createReservationDateEntityList(
                reservationModel.getArrivalDate(), reservationModel.getDepartureDate()));

        CompletableFuture<ReservationEntity> reservationFuture = reservationRepository
                .updateReservation(reservationEntity);
        ReservationEntity foundReservation = reservationFuture.get();
        return CompletableFuture.completedFuture(
                new ReservationModel(foundReservation.getId().toString(), foundReservation.getBase36Id(),
                        foundReservation.getArrivaleDate(), foundReservation.getDepartureDate()));
    }

    @Async
    public CompletableFuture<Boolean> deleteReservation(
            @NotNull @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX) String base36Id) throws Throwable {
        reservationRepository.deleteReservation(base36Id).get();
        return CompletableFuture.completedFuture(true);
    }
}
