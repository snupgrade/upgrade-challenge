package com.upgrade.challenge.reservationservice.controller;

import java.time.LocalDate;
import java.util.List;

import com.upgrade.challenge.commonservicelibrary.service.model.ReservationModel;
import com.upgrade.challenge.reservationservice.handler.ReservationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/campsites")
public class ReservationController {

    @Autowired
    private ReservationHandler reservationHandler;

    @GetMapping("/{campsiteId}/reservations")
    public List<LocalDate> getReservationDates(@PathVariable("campsiteId") String campsiteId,
            @RequestParam(value = "arrivalDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate arrivalDate,
            @RequestParam(value = "departureDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate departureDate)
            throws Throwable {
        return reservationHandler.getReservationDates(campsiteId, arrivalDate, departureDate).get();
    }

    @PostMapping("/{campsiteId}/reservations")
    public ReservationModel addReservation(@PathVariable("campsiteId") String campsiteId,
            @RequestBody ReservationModel reservationModel) throws Throwable {
        return reservationHandler.createReservation(campsiteId, reservationModel).get();
    }

    @GetMapping("/reservations/{base36Id}")
    public ReservationModel getCampsiteReservation(@PathVariable("base36Id") String base36Id) throws Throwable {
        return reservationHandler.getReservation(base36Id).get();
    }

    @PutMapping("/reservations/{base36Id}")
    public ReservationModel updateReservation(@PathVariable("base36Id") String base36Id,
            @RequestBody ReservationModel reservationModel) throws Throwable {
        return reservationHandler.updateReservation(base36Id, reservationModel).get();
    }

    @DeleteMapping("/reservations/{base36Id}")
    public void deleteReservation(@PathVariable("base36Id") String base36Id) throws Throwable {
        reservationHandler.deleteReservation(base36Id).get();
    }
}
