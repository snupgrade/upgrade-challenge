package com.upgrade.challenge.commonservicelibrary.service.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

public class ReservationModel {

    @Pattern(regexp = CommonRegex.UUID_REGEX, message = "ReservationId format should be a uuid4.")
    private String reservationId;

    @NotNull(message = "User information is mandatory")
    private UserModel user;

    @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX, message = "Base 36 id is not valid.")
    private String base36Id;

    @NotNull(message = "Arrival date is mandatory")
    private LocalDate arrivalDate;

    @NotNull(message = "Departure date is mandatory")
    private LocalDate departureDate;

    public ReservationModel() {
    }

    public ReservationModel(String reservationId, String base36Id, LocalDate arrivalDate, LocalDate departureDate) {
        this.reservationId = reservationId;
        this.base36Id = base36Id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public ReservationModel(UserModel user, String base36Id, LocalDate arrivalDate, LocalDate departureDate) {
        this.user = user;
        this.base36Id = base36Id;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getBase36Id() {
        return this.base36Id;
    }

    public void setBase36Id(String base36Id) {
        this.base36Id = base36Id;
    }

    public LocalDate getArrivalDate() {
        return this.arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return this.departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
