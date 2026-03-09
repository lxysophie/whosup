package com.whosup.service;

import com.whosup.dto.request.CreateActivityRequest;
import com.whosup.dto.request.UpdateActivityRequest;
import com.whosup.dto.response.ActivityDetailResponse;
import com.whosup.dto.response.ActivityResponse;
import com.whosup.entity.*;
import com.whosup.exception.ForbiddenException;
import com.whosup.exception.ResourceNotFoundException;
import com.whosup.exception.UnprocessableException;
import com.whosup.mapper.ActivityMapper;
import com.whosup.repository.ActivityRepository;
import com.whosup.repository.ParticipationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ParticipationRepository participationRepository;

    public ActivityService(ActivityRepository activityRepository,
                           ParticipationRepository participationRepository) {
        this.activityRepository = activityRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponse> findAll(ActivityStatus status, ActivityCategory category,
                                           String search, Pageable pageable) {
        return activityRepository.findWithFilters(status, category, search, pageable)
                .map(activity -> {
                    int count = participationRepository.countByActivityId(activity.getId());
                    return ActivityMapper.toResponse(activity, count);
                });
    }

    @Transactional(readOnly = true)
    public ActivityDetailResponse findById(Long id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
        List<Participation> participations = participationRepository.findByActivityId(id);
        return ActivityMapper.toDetailResponse(activity, participations);
    }

    @Transactional
    public ActivityDetailResponse create(CreateActivityRequest request, User creator) {
        ActivityCategory category = parseCategory(request.category());

        Activity activity = new Activity();
        activity.setTitle(request.title());
        activity.setDescription(request.description());
        activity.setLocation(request.location());
        activity.setActivityDate(request.activityDate());
        activity.setCategory(category);
        activity.setMaxParticipants(request.maxParticipants());
        activity.setCreator(creator);

        activity = activityRepository.save(activity);
        return ActivityMapper.toDetailResponse(activity, List.of());
    }

    @Transactional
    public ActivityDetailResponse update(Long id, UpdateActivityRequest request, User currentUser) {
        Activity activity = getActivityAsCreator(id, currentUser);

        if (request.title() != null) activity.setTitle(request.title());
        if (request.description() != null) activity.setDescription(request.description());
        if (request.location() != null) activity.setLocation(request.location());
        if (request.activityDate() != null) activity.setActivityDate(request.activityDate());
        if (request.category() != null) activity.setCategory(parseCategory(request.category()));
        if (request.maxParticipants() != null) {
            int currentCount = participationRepository.countByActivityId(id) + 1;
            if (request.maxParticipants() < currentCount) {
                throw new UnprocessableException(
                        "Cannot reduce max participants below current count (" + currentCount + ")");
            }
            activity.setMaxParticipants(request.maxParticipants());
        }

        activity = activityRepository.save(activity);
        List<Participation> participations = participationRepository.findByActivityId(id);
        return ActivityMapper.toDetailResponse(activity, participations);
    }

    @Transactional
    public void delete(Long id, User currentUser) {
        Activity activity = getActivityAsCreator(id, currentUser);
        activityRepository.delete(activity);
    }

    @Transactional
    public ActivityDetailResponse cancel(Long id, User currentUser) {
        Activity activity = getActivityAsCreator(id, currentUser);
        if (activity.getStatus() == ActivityStatus.CANCELLED
                || activity.getStatus() == ActivityStatus.COMPLETED) {
            throw new UnprocessableException("Cannot cancel a " + activity.getStatus() + " activity");
        }
        activity.setStatus(ActivityStatus.CANCELLED);
        activity = activityRepository.save(activity);
        List<Participation> participations = participationRepository.findByActivityId(id);
        return ActivityMapper.toDetailResponse(activity, participations);
    }

    @Transactional
    public ActivityDetailResponse complete(Long id, User currentUser) {
        Activity activity = getActivityAsCreator(id, currentUser);
        if (activity.getStatus() == ActivityStatus.CANCELLED
                || activity.getStatus() == ActivityStatus.COMPLETED) {
            throw new UnprocessableException("Cannot complete a " + activity.getStatus() + " activity");
        }
        activity.setStatus(ActivityStatus.COMPLETED);
        activity = activityRepository.save(activity);
        List<Participation> participations = participationRepository.findByActivityId(id);
        return ActivityMapper.toDetailResponse(activity, participations);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> findByCreator(Long creatorId) {
        return activityRepository.findByCreatorId(creatorId).stream()
                .map(activity -> {
                    int count = participationRepository.countByActivityId(activity.getId());
                    return ActivityMapper.toResponse(activity, count);
                })
                .toList();
    }

    private Activity getActivityAsCreator(Long id, User currentUser) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
        if (!activity.getCreator().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Only the creator can modify this activity");
        }
        return activity;
    }

    private ActivityCategory parseCategory(String category) {
        try {
            return ActivityCategory.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new UnprocessableException("Invalid category: " + category);
        }
    }
}
