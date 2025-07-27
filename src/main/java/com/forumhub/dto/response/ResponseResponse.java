package com.forumhub.dto.response;

import com.forumhub.entity.Response;

import java.time.LocalDateTime;

public record ResponseResponse(
        Long id,
        String message,
        TopicResponse.UserSummary author,
        Boolean solution,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public ResponseResponse(Response response) {
        this(
                response.getId(),
                response.getMessage(),
                new TopicResponse.UserSummary(response.getAuthor()),
                response.getSolution(),
                response.getCreatedAt(),
                response.getUpdatedAt()
        );
    }
}
