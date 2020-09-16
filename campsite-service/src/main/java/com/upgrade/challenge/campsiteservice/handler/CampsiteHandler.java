package com.upgrade.challenge.campsiteservice.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.campsiteservice.repository.CampsiteRepository;
import com.upgrade.challenge.campsiteservice.repository.entity.CampsiteEntity;
import com.upgrade.challenge.commonservicelibrary.service.model.CampsiteModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CampsiteHandler {
    static Logger log = LoggerFactory.getLogger(CampsiteHandler.class);

    @Autowired
    private CampsiteRepository campsiteRepository;

    @Async
    public CompletableFuture<List<CampsiteModel>> getCampsites() throws Throwable {
        CompletableFuture<List<CampsiteEntity>> campsiteEntitiesFuture = campsiteRepository.findAllCampsites();

        List<CampsiteEntity> campsiteEntities = campsiteEntitiesFuture.get();
        if (campsiteEntities.isEmpty()) {
            return CompletableFuture.failedFuture(new EntityNotFoundException("No campsites found."));
        }

        List<CampsiteModel> campsiteModels = new ArrayList<>();
        for (CampsiteEntity campsiteEntity : campsiteEntities) {
            campsiteModels.add(new CampsiteModel(campsiteEntity.getId().toString(), campsiteEntity.getName()));
        }

        return CompletableFuture.completedFuture(campsiteModels);
    }

    @Async
    public CompletableFuture<CampsiteModel> getCampsite(
            @NotBlank @Pattern(regexp = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}") String campsiteId)
            throws Throwable {
        CompletableFuture<CampsiteEntity> campsiteEntityFuture = campsiteRepository.findByCampsiteId(campsiteId);
        CampsiteEntity campsiteEntity = campsiteEntityFuture.get();

        if (campsiteEntity == null) {
            return CompletableFuture
                    .failedFuture(new EntityNotFoundException("No campsite found with the id : " + campsiteId));
        }

        return CompletableFuture
                .completedFuture(new CampsiteModel(campsiteEntity.getId().toString(), campsiteEntity.getName()));
    }

}
