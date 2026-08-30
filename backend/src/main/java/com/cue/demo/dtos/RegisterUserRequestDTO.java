package com.cue.demo.dtos;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequestDTO (
    @NotBlank(message = "username cannot be empty") String username,
    @NotBlank(message = "password cannot be empty") String password,
    @NotBlank(message = "firstName cannot be empty") String firstName,
    @NotBlank(message = "lastName cannot be empty") String lastName,
    @NotBlank(message = "email cannot be empty") String email,
    @NotBlank(message = "city cannot be empty") String city
){
}
