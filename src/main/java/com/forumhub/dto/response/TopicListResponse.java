package com.forumhub.dto.response;

import com.forumhub.entity.Topic;

import java.time.LocalDateTime;

public record TopicListResponse(
        Long id,
        String title,
        String status,
        String authorName,
        String courseName,
        Integer responseCount,
        LocalDateTime createdAt
) {
    public TopicListResponse(Topic topic) {
        this(
                topic.getId(),
                topic.getTitle(),
                topic.getStatus().name(),
                topic.getAuthor().getName(),
                topic.getCourse().getName(),
                topic.getResponses() != null ? topic.getResponses().size() : 0,
                topic.getCreatedAt()
        );
    }
}
