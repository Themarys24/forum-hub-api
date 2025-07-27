package com.forumhub.dto.response;

import com.forumhub.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role,
        Boolean active,
        LocalDateTime createdAt
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getActive(),
                user.getCreatedAt()
        );
    }
}

