package com.upgrade.challenge.reservationservice.repository.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

import org.hibernate.annotations.Type;

@Entity(name = "Reservation")
@Table(name = "reservation", schema = "reservation")
public class ReservationEntity {
    private static final String BASE_36_ID_CHAR_CHOISE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @Pattern(regexp = CommonRegex.UUID_REGEX)
    @Column(name = "campsite_id")
    private String campsiteId;

    @Pattern(regexp = CommonRegex.UUID_REGEX)
    @Column(name = "user_id")
    private String userId;

    @Pattern(regexp = CommonRegex.BASE_36_ID_REGEX)
    @Column(name = "base_36_id")
    private String base36Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_status")
    private ReservationStatusEntity reservationStatus;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReservationDateEntity> reservationDates;

    public ReservationEntity() {
    }

    public ReservationEntity(String campsiteId) {
        this.campsiteId = campsiteId;
    }

    public ReservationEntity(String campsiteId, String base36Id) {
        this.campsiteId = campsiteId;
        this.base36Id = base36Id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCampsiteId() {
        return campsiteId;
    }

    public void setCampsiteId(String campsiteId) {
        this.campsiteId = campsiteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBase36Id() {
        return base36Id;
    }

    public void setBase36Id(String base36Id) {
        this.base36Id = base36Id;
    }

    public ReservationStatusEntity getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatusEntity reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public List<ReservationDateEntity> getReservationDates() {
        return reservationDates;
    }

    public LocalDate getArrivaleDate() {
        if (!reservationDates.isEmpty()) {
            Optional<Date> arrivalDate = reservationDates.stream().map(u -> u.getReservationDate())
                    .min(Date::compareTo);
            if (arrivalDate.isPresent()) {
                return arrivalDate.get().toLocalDate();
            }
        }
        return null;
    }

    public LocalDate getDepartureDate() {
        if (!reservationDates.isEmpty()) {
            Optional<Date> departureDate = reservationDates.stream().map(u -> u.getReservationDate())
                    .max(Date::compareTo);
            if (departureDate.isPresent()) {
                return departureDate.get().toLocalDate();
            }
        }
        return null;
    }

    public void setReservationDates(List<ReservationDateEntity> reservationDates) {
        this.reservationDates = reservationDates;
    }

    public static String generateBase36Id() {
        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {
            sb.append(BASE_36_ID_CHAR_CHOISE.charAt((int) (BASE_36_ID_CHAR_CHOISE.length() * Math.random())));
        }
        return sb.toString();
    }
}
