package com.forumhub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseRequest(
        @NotBlank(message = "Course name is required")
        @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
        String name,

        @Size(max = 50, message = "Category must not exceed 50 characters")
        String category,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description

) {}

