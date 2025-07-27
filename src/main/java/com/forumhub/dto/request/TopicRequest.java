package com.forumhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TopicRequest(
        @NotBlank(message = "Title is required")
        @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
        String title,

        @NotBlank(message = "Message is required")
        @Size(min = 10, message = "Message must have at least 10 characters")
        String message,

        @NotNull(message = "Course ID is required")
        Long courseId

) {}

