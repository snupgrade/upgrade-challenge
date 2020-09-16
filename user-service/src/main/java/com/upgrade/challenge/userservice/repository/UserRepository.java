package com.upgrade.challenge.userservice.repository;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.upgrade.challenge.userservice.repository.entity.UserEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;

public interface UserRepository extends CrudRepository<UserEntity, UUID> {

    @Async
    CompletableFuture<UserEntity> findByEmailIgnoreCase(@NotBlank @Email String email);
    
    <S extends UserEntity> S save(@NotNull @Valid S entity);
}
