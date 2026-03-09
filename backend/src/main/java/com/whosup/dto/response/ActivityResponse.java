package com.whosup.dto.response;

import java.time.Instant;

public record ActivityResponse(
        Long id,
        String title,
        String location,
        Instant activityDate,
        String category,
        String status,
        int maxParticipants,
        int currentParticipants,
        CreatorInfo creator,
        Instant createdAt
) {
    public record CreatorInfo(Long id, String displayName) {}
}
