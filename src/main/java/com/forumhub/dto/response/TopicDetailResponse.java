package com.forumhub.dto.response;

import com.forumhub.entity.Topic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record TopicDetailResponse(
        Long id,
        String title,
        String message,
        String status,
        UserSummary author,
        CourseSummary course,
        List<ResponseSummary> responses,
        Integer responseCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public TopicDetailResponse(Topic topic) {
        this(
                topic.getId(),
                topic.getTitle(),
                topic.getMessage(),
                topic.getStatus().name(),
                new UserSummary(topic.getAuthor()),
                new CourseSummary(topic.getCourse()),
                topic.getResponses() != null
                        ? topic.getResponses().stream()
                        .map(ResponseSummary::new)
                        .collect(Collectors.toList())
                        : List.of(),
                topic.getResponses() != null ? topic.getResponses().size() : 0,
                topic.getCreatedAt(),
                topic.getUpdatedAt()
        );
    }

    // Nested records for related data
    public record UserSummary(
            Long id,
            String name,
            String email,
            String role
    ) {
        public UserSummary(com.forumhub.entity.User user) {
            this(user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        }
    }

    public record CourseSummary(
            Long id,
            String name,
            String category,
            String description
    ) {
        public CourseSummary(com.forumhub.entity.Course course) {
            this(course.getId(), course.getName(), course.getCategory(), course.getDescription());
        }
    }

    public record ResponseSummary(
            Long id,
            String message,
            UserSummary author,
            Boolean solution,
            LocalDateTime createdAt
    ) {
        public ResponseSummary(com.forumhub.entity.Response response) {
            this(
                    response.getId(),
                    response.getMessage(),
                    new UserSummary(response.getAuthor()),
                    response.getSolution(),
                    response.getCreatedAt()
            );
        }
    }
}