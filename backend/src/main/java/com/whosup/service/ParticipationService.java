package com.whosup.service;

import com.whosup.dto.response.ActivityResponse;
import com.whosup.entity.*;
import com.whosup.exception.ConflictException;
import com.whosup.exception.ResourceNotFoundException;
import com.whosup.exception.UnprocessableException;
import com.whosup.mapper.ActivityMapper;
import com.whosup.repository.ActivityRepository;
import com.whosup.repository.ParticipationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final ActivityRepository activityRepository;

    public ParticipationService(ParticipationRepository participationRepository,
                                ActivityRepository activityRepository) {
        this.participationRepository = participationRepository;
        this.activityRepository = activityRepository;
    }

    @Transactional
    public void join(Long activityId, User user) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        if (activity.getCreator().getId().equals(user.getId())) {
            throw new ConflictException("You are the creator of this activity");
        }

        if (activity.getStatus() != ActivityStatus.OPEN) {
            throw new UnprocessableException("Cannot join a " + activity.getStatus() + " activity");
        }

        if (participationRepository.existsByUserIdAndActivityId(user.getId(), activityId)) {
            throw new ConflictException("Already joined this activity");
        }

        int currentCount = participationRepository.countByActivityId(activityId) + 1; // +1 for creator
        if (currentCount >= activity.getMaxParticipants()) {
            throw new UnprocessableException("Activity is full");
        }

        participationRepository.save(new Participation(user, activity));

        // Auto-transition to FULL if now at capacity
        int newCount = currentCount + 1;
        if (newCount >= activity.getMaxParticipants()) {
            activity.setStatus(ActivityStatus.FULL);
            activityRepository.save(activity);
        }
    }

    @Transactional
    public void leave(Long activityId, User user) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        Participation participation = participationRepository
                .findByUserIdAndActivityId(user.getId(), activityId)
                .orElseThrow(() -> new ConflictException("Not a participant of this activity"));

        participationRepository.delete(participation);

        // Auto-transition from FULL to OPEN
        if (activity.getStatus() == ActivityStatus.FULL) {
            activity.setStatus(ActivityStatus.OPEN);
            activityRepository.save(activity);
        }
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> getJoinedActivities(Long userId) {
        return participationRepository.findByUserId(userId).stream()
                .map(p -> {
                    Activity activity = p.getActivity();
                    int count = participationRepository.countByActivityId(activity.getId());
                    return ActivityMapper.toResponse(activity, count);
                })
                .toList();
    }
}
