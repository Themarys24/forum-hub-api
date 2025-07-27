package com.forumhub.dto.response;

import com.forumhub.entity.Topic;

import java.time.LocalDateTime;

public record TopicResponse(
        Long id,
        String title,
        String message,
        String status,
        UserSummary author,
        CourseSummary course,
        Integer responseCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public TopicResponse(Topic topic) {
        this(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getStatus().name(),
                new UserSummary(topic.getAuthor()),
                new CourseSummary(topic.getCourse()),
                topic.getResponses() != null ? topic.getResponses().size() : 0,
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }

    // Nested records for summary data
    public record UserSummary(
            Long id,
            String name,
            String email
    ) {
        public UserSummary(com.forumhub.entity.User user) {
            this(user.getId(), user.getName(), user.getEmail());
        }
    }

    public record CourseSummary(
            Long id,
            String name,
            String category
    ) {
        public CourseSummary(com.forumhub.entity.Course course) {
            this(course.getId(), course.getName(), course.getCategory());
        }
    }
}


