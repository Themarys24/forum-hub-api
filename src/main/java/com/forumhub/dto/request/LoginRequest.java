package com.forumhub.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank(message = "Email is required")
                           @Email(message = "Email must have a valid format")
                           String email,

                           @NotBlank(message = "Password is required")
                           String password) {}
