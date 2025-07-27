package com.forumhub.dto.response;

import com.forumhub.entity.Course;

import java.time.LocalDateTime;

public record CourseResponse(Long id,
                             String name,
                             String category,
                             String description,
                             Boolean active,
                             Integer topicCount,
                             LocalDateTime createdAt
) {
    public CourseResponse(Course course) {
        this(
                course.getId(),
                course.getName(),
                course.getCategory(),
                course.getDescription(),
                course.getActive(),
                course.getTopicCount(),
                course.getCreatedAt()
        );
    }
}
