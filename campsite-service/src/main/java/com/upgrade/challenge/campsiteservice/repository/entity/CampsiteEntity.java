package com.upgrade.challenge.campsiteservice.repository.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

import org.hibernate.annotations.Type;

@Entity(name = "Campsite")
@Table(name = "campsite", schema = "campsite")
public class CampsiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Pattern(regexp = CommonRegex.UUID_REGEX, message = "Campsite id is not valid.")
    private UUID id;

    @Pattern(regexp = CommonRegex.DATABASE_TYPE_REGEX, message = "Campsite name is not valid.")
    @Column(name = "name", nullable = false)
    private String name;

    protected CampsiteEntity() {
    }

    public CampsiteEntity(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
