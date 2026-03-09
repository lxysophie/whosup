package com.whosup.dto.response;

public record AuthResponse(
        String token,
        UserResponse user
) {}
