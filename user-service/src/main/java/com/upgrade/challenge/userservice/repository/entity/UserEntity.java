package com.upgrade.challenge.userservice.repository.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

@Entity(name = "User")
@Table(name = "users", schema = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 128, message = "First name between 2 and 128 characters.")
    @Pattern(regexp = "^[\\p{L} ,.'-]*$", message = "First name is not valid.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 128, message = "Last name between 2 and 128 characters.")
    @Pattern(regexp = "^[\\p{L} ,.'-]*$", message = "Last name is not valid.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank
    @Email(message = "Email is not valid.")
    @Column(name = "email", nullable = false)
    private String email;

    protected UserEntity() {
    }

    public UserEntity(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public UUID getId() {
        return id;
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
