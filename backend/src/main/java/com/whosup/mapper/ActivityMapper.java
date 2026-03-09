package com.whosup.mapper;

import com.whosup.dto.response.ActivityDetailResponse;
import com.whosup.dto.response.ActivityResponse;
import com.whosup.entity.Activity;
import com.whosup.entity.Participation;

import java.util.List;

public final class ActivityMapper {

    private ActivityMapper() {}

    public static ActivityResponse toResponse(Activity activity, int participantCount) {
        return new ActivityResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getLocation(),
                activity.getActivityDate(),
                activity.getCategory().name(),
                activity.getStatus().name(),
                activity.getMaxParticipants(),
                participantCount + 1, // +1 for creator
                new ActivityResponse.CreatorInfo(
                        activity.getCreator().getId(),
                        activity.getCreator().getDisplayName()
                ),
                activity.getCreatedAt()
        );
    }

    public static ActivityDetailResponse toDetailResponse(Activity activity,
                                                           List<Participation> participations) {
        List<ActivityDetailResponse.ParticipantInfo> participants = participations.stream()
                .map(p -> new ActivityDetailResponse.ParticipantInfo(
                        p.getUser().getId(),
                        p.getUser().getDisplayName(),
                        p.getJoinedAt()
                ))
                .toList();

        return new ActivityDetailResponse(
                activity.getId(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getLocation(),
                activity.getActivityDate(),
                activity.getCategory().name(),
                activity.getStatus().name(),
                activity.getMaxParticipants(),
                participations.size() + 1, // +1 for creator
                new ActivityResponse.CreatorInfo(
                        activity.getCreator().getId(),
                        activity.getCreator().getDisplayName()
                ),
                activity.getCreatedAt(),
                activity.getUpdatedAt(),
                participants
        );
    }
}
