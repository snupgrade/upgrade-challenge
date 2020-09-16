package com.upgrade.challenge.userservice.handler;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;
import com.upgrade.challenge.commonservicelibrary.service.model.UserModel;
import com.upgrade.challenge.userservice.repository.UserRepository;
import com.upgrade.challenge.userservice.repository.entity.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserHandler {

    static Logger log = LoggerFactory.getLogger(UserHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Async
    public CompletableFuture<UserModel> getUserByEmail(@NotBlank @Email @Valid String email) throws Throwable {
        CompletableFuture<UserEntity> userEntityFuture = userRepository.findByEmailIgnoreCase(email);
        UserEntity userEntity = userEntityFuture.get();
        if (userEntity == null) {
            return CompletableFuture
                    .failedFuture(new EntityNotFoundException("Can't find user with the email : " + email));
        }
        return CompletableFuture.completedFuture(new UserModel(userEntity.getId().toString(), userEntity.getFirstName(),
                userEntity.getLastName(), userEntity.getEmail()));
    }

    @Async
    public CompletableFuture<UserModel> getUserById(@NotBlank @Pattern(regexp = CommonRegex.UUID_REGEX) String userId)
            throws Throwable {
        Optional<UserEntity> userEntityOption = userRepository.findById(UUID.fromString(userId));
        if (userEntityOption.isPresent()) {
            UserEntity userEntity = userEntityOption.get();
            return CompletableFuture.completedFuture(new UserModel(userEntity.getId().toString(),
                    userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail()));
        }
        return CompletableFuture.failedFuture(new EntityNotFoundException("Can't find user with the id : " + userId));
    }

    @Async
    public CompletableFuture<UserModel> addUser(@NotNull @Valid UserModel userModel) throws Throwable {
        CompletableFuture<UserEntity> userEntityFuture = userRepository.findByEmailIgnoreCase(userModel.getEmail());
        UserEntity userEntity = userEntityFuture.get();
        if (userEntity == null) {
            userEntity = userRepository
                    .save(new UserEntity(userModel.getFirstName(), userModel.getLastName(), userModel.getEmail()));
            return CompletableFuture.completedFuture(new UserModel(userEntity.getId().toString(),
                    userEntity.getFirstName(), userEntity.getLastName(), userEntity.getEmail()));
        }
        return CompletableFuture.failedFuture(
                new EntityExistsException("User Already exists with the email : " + userModel.getEmail()));
    }
}
