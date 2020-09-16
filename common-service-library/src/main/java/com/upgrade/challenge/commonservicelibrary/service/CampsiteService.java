package com.upgrade.challenge.commonservicelibrary.service;

import java.util.concurrent.CompletableFuture;

import com.upgrade.challenge.commonservicelibrary.service.model.CampsiteModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CampsiteService {

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<CampsiteModel> getCampsiteById(String campsiteId) {
        ResponseEntity<CampsiteModel> campsiteResponseEntity = this.restTemplate
                .getForEntity("http://campsite-service/v1/campsites/" + campsiteId, CampsiteModel.class);
        if (campsiteResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return CompletableFuture.completedFuture(campsiteResponseEntity.getBody());
        }
        return CompletableFuture.completedFuture(null);
    }

}
