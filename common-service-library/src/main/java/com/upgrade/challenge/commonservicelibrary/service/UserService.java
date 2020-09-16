package com.upgrade.challenge.commonservicelibrary.service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.List;

import javax.validation.Valid;

import com.upgrade.challenge.commonservicelibrary.service.model.UserModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public CompletableFuture<UserModel> getUserById(String userId) {
        ResponseEntity<UserModel> userResponseEntity = this.restTemplate
                .getForEntity("http://user-service/v1/users/" + userId, UserModel.class);
        if (userResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return CompletableFuture.completedFuture(userResponseEntity.getBody());
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<UserModel>> getUserByEmail(String email) {
        try {
            ResponseEntity<UserModel[]> userResponseEntity = this.restTemplate
                    .getForEntity("http://user-service/v1/users?email=" + email, UserModel[].class);
            if (userResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
                return CompletableFuture.completedFuture(Arrays.asList(userResponseEntity.getBody()));
            }
        } catch (HttpClientErrorException e) {
            if (!e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw e;
            }
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<UserModel> createUser(@Valid UserModel userModel) {
        ResponseEntity<UserModel> userResponseEntity = this.restTemplate.postForEntity("http://user-service/v1/users",
                userModel, UserModel.class);
        if (userResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return CompletableFuture.completedFuture(userResponseEntity.getBody());
        }
        return CompletableFuture.completedFuture(null);
    }

}
