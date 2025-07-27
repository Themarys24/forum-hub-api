package com.forumhub.dto.response;

public record TokenResponse(
        String token,
        String type,
        UserResponse user

) {
    public TokenResponse(String token, UserResponse user) {
        this("Bearer", token, user);
    }

}
