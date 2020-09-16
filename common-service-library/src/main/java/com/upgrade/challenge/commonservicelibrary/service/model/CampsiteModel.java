package com.upgrade.challenge.commonservicelibrary.service.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

public class CampsiteModel {

    @Pattern(regexp = CommonRegex.UUID_REGEX, message = "Campsite Id format should be a uuid4.")
    private String campsiteId;

    @NotBlank
    @Pattern(regexp = CommonRegex.DATABASE_TYPE_REGEX, message = "Campsite name is not valid.")
    private String name;

    public CampsiteModel() {
    }

    public CampsiteModel(String campsiteId, String name) {
        this.campsiteId = campsiteId;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCampsiteId() {
        return this.campsiteId;
    }

    public void setCampsiteId(String campsiteId) {
        this.campsiteId = campsiteId;
    }

}
