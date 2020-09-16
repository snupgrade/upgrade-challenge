package com.upgrade.challenge.reservationservice.validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {

    public boolean validateReservationDates(LocalDate arrivalLocalDate, LocalDate departureLocalDate) {

        if (arrivalLocalDate == null || departureLocalDate == null) {
            return false;
        }

        if (ChronoUnit.DAYS.between(arrivalLocalDate, departureLocalDate) > 3) {
            return false;
        }

        if (arrivalLocalDate.isBefore(LocalDate.now().plusDays(1))) {
            return false;
        }

        if (!departureLocalDate.isBefore(LocalDate.now().plusMonths(1))) {
            return false;
        }

        if (arrivalLocalDate.isAfter(departureLocalDate)) {
            return false;
        }

        return true;
    }
}
