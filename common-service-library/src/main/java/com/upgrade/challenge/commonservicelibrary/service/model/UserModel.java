package com.upgrade.challenge.commonservicelibrary.service.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.upgrade.challenge.commonservicelibrary.constant.CommonRegex;

public class UserModel {

    @Pattern(regexp = CommonRegex.UUID_REGEX, message = "UserId format should be a uuid4.")
    private String userId;

    @NotBlank
    @Size(min = 2, max = 128, message = "First name between 2 and 128 characters.")
    @Pattern(regexp = CommonRegex.NAME_REGEX, message = "First name is not valid.")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 128, message = "Last name between 2 and 128 characters.")
    @Pattern(regexp = CommonRegex.NAME_REGEX, message = "Last name is not valid.")
    private String lastName;

    @NotBlank
    @Email(message = "Email is not valid.")
    private String email;

    public UserModel() {
    }

    public UserModel(String userId, String firstName, String lastName, String email) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

}
