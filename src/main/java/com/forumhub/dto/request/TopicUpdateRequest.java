package com.forumhub.dto.request;

import jakarta.validation.constraints.Size;

public record TopicUpdateRequest(@Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
                                 String title,

                                 @Size(min = 10, max = 2000, message = "Message must be between 10 and 2000 characters")
                                 String message) {
}
