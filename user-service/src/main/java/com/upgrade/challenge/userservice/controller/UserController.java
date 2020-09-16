package com.upgrade.challenge.userservice.controller;

import java.util.Arrays;
import java.util.List;

import com.upgrade.challenge.commonservicelibrary.service.model.UserModel;
import com.upgrade.challenge.userservice.handler.UserHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserHandler userHandler;

    @GetMapping("")
    public List<UserModel> getUsers(@RequestParam String email) throws Throwable {
        return Arrays.asList(userHandler.getUserByEmail(email).get());
    }

    @GetMapping("/{userId}")
    public UserModel getUser(@PathVariable("userId") String userId) throws Throwable {
        return userHandler.getUserById(userId).get();
    }

    @PostMapping("")
    public UserModel addUser(@RequestBody UserModel userModel) throws Throwable {
        return userHandler.addUser(userModel).get();
    }
}
