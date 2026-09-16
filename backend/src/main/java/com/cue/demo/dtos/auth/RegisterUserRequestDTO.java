package com.cue.demo.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO (
    @Size(min = 3)@NotBlank(message = "username cannot be empty") String username,
    @Size(min = 8) @NotBlank(message = "password cannot be empty") String password,
    @NotBlank(message = "firstName cannot be empty") String firstName,
    @NotBlank(message = "lastName cannot be empty") String lastName,
    @Email @NotBlank(message = "email cannot be empty") String email,
    @NotBlank(message = "city cannot be empty") String city
){
}
