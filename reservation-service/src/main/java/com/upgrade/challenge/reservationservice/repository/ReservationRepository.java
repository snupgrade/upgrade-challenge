package com.upgrade.challenge.reservationservice.repository;

import java.util.UUID;

import com.upgrade.challenge.reservationservice.repository.entity.ReservationEntity;

import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID>, ReservationRepositoryCustom {

}
