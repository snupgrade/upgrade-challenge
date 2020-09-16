package com.upgrade.challenge.campsiteservice.controller;

import java.util.List;

import com.upgrade.challenge.campsiteservice.handler.CampsiteHandler;
import com.upgrade.challenge.commonservicelibrary.service.model.CampsiteModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/campsites")
public class CampsiteController {

    @Autowired
    private CampsiteHandler campsiteHandler;

    @GetMapping("")
    public List<CampsiteModel> getCampsites() throws Throwable {
        return campsiteHandler.getCampsites().get();
    }

    @GetMapping("/{campsiteId}")
    public CampsiteModel getCampsite(@PathVariable String campsiteId) throws Throwable {
        return campsiteHandler.getCampsite(campsiteId).get();
    }
}
