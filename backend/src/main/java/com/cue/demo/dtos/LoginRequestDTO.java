package com.cue.demo.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank(message = "username cannot be empty")
                              String username,
                              @NotBlank(message = "password cannot be empty")
                              String password) {}
