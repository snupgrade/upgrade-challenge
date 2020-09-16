package com.upgrade.challenge.campsiteservice.repository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.campsiteservice.repository.entity.CampsiteEntity;
import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

public interface CampsiteRepository extends CrudRepository<CampsiteEntity, UUID> {

    @Async
    @Query("SELECT c FROM Campsite c WHERE LOWER(c.id) = LOWER(?1)")
    CompletableFuture<CampsiteEntity> findByCampsiteId(
            @NotBlank @Pattern(regexp = CommonRegex.UUID_REGEX) String campsiteId);

    @Async
    @Query("select c from Campsite c")
    CompletableFuture<List<CampsiteEntity>> findAllCampsites();
}
