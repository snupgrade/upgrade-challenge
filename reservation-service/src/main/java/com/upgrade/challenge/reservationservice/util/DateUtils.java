package com.upgrade.challenge.reservationservice.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.upgrade.challenge.reservationservice.repository.entity.ReservationDateEntity;

public class DateUtils {

    public static List<LocalDate> getDateListFromRange(LocalDate arrivalDate, LocalDate departureDate) {
        List<LocalDate> dates = new ArrayList<>();

        for (LocalDate date = arrivalDate; !date.isAfter(departureDate); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }

    public static List<ReservationDateEntity> createReservationDateEntityList(LocalDate arrivalDate,
            LocalDate departureDate) {
        List<ReservationDateEntity> reservationDateEntities = new ArrayList<>();
        List<LocalDate> dates = DateUtils.getDateListFromRange(arrivalDate, departureDate);

        for (LocalDate date : dates) {
            reservationDateEntities.add(new ReservationDateEntity(Date.valueOf(date)));
        }
        return reservationDateEntities;
    }

    public static List<LocalDate> findAvailableDatesBetweenRange(LocalDate arrivalDate, LocalDate departureDate,
            List<LocalDate> reservedDates) {
        List<LocalDate> availableDates = new ArrayList<>();
        for (LocalDate date = arrivalDate; !date.isAfter(departureDate); date = date.plusDays(1)) {
            if (!reservedDates.contains(date)) {
                availableDates.add(date);
            }
        }
        return availableDates;
    }
}
