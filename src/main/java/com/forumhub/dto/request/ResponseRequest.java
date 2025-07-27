package com.forumhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResponseRequest(
        @NotBlank(message = "Response message is required")
        @Size(min = 10, message = "Response message must have at least 10 characters")
        String message,

        @NotNull(message = "Topic ID is required")
        Long topicId

) {}

