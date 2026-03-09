package com.whosup.dto.response;

import java.time.Instant;
import java.util.List;

public record ActivityDetailResponse(
        Long id,
        String title,
        String description,
        String location,
        Instant activityDate,
        String category,
        String status,
        int maxParticipants,
        int currentParticipants,
        ActivityResponse.CreatorInfo creator,
        Instant createdAt,
        Instant updatedAt,
        List<ParticipantInfo> participants
) {
    public record ParticipantInfo(Long userId, String displayName, Instant joinedAt) {}
}
