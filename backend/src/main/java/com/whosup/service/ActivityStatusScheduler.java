package com.whosup.service;

import com.whosup.repository.ActivityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
public class ActivityStatusScheduler {

    private final ActivityRepository activityRepository;

    public ActivityStatusScheduler(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Scheduled(fixedRate = 900000) // every 15 minutes
    @Transactional
    public void completeExpiredActivities() {
        activityRepository.completeExpiredActivities(Instant.now());
    }
}
