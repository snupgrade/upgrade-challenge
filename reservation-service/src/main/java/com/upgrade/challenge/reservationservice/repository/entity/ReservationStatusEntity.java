package com.upgrade.challenge.reservationservice.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

@Entity(name = "ReservationStatus")
@Table(name = "reservation_status", schema = "reservation")
public class ReservationStatusEntity {

    public enum ReservationStatusEnum {
        PENDING, RESERVED, CANCELED

    }

    @Id
    @NotBlank
    @Pattern(regexp = CommonRegex.DATABASE_TYPE_REGEX)
    @Column(name = "reservation_status")
    private String reservationStatus;

    public ReservationStatusEntity() {
    }

    public ReservationStatusEntity(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
