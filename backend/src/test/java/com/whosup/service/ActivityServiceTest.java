package com.whosup.service;

import com.whosup.dto.request.CreateActivityRequest;
import com.whosup.dto.response.ActivityDetailResponse;
import com.whosup.entity.*;
import com.whosup.exception.ForbiddenException;
import com.whosup.exception.UnprocessableException;
import com.whosup.repository.ActivityRepository;
import com.whosup.repository.ParticipationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ParticipationRepository participationRepository;

    @InjectMocks
    private ActivityService activityService;

    private User creator;
    private User otherUser;
    private Activity activity;

    @BeforeEach
    void setUp() {
        creator = new User("creator@test.com", "password", "Creator");
        creator.setId(1L);

        otherUser = new User("other@test.com", "password", "Other");
        otherUser.setId(2L);

        activity = new Activity();
        activity.setId(1L);
        activity.setTitle("Test Activity");
        activity.setLocation("Test Location");
        activity.setActivityDate(Instant.now().plus(1, ChronoUnit.DAYS));
        activity.setCategory(ActivityCategory.SPORTS);
        activity.setMaxParticipants(4);
        activity.setStatus(ActivityStatus.OPEN);
        activity.setCreator(creator);
    }

    @Test
    void create_shouldSaveAndReturnActivity() {
        CreateActivityRequest request = new CreateActivityRequest(
                "Tennis Match", "Fun match", "Park Courts",
                Instant.now().plus(1, ChronoUnit.DAYS), "SPORTS", 4
        );

        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> {
            Activity saved = inv.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        ActivityDetailResponse response = activityService.create(request, creator);

        assertEquals("Tennis Match", response.title());
        assertEquals("SPORTS", response.category());
        assertEquals(1, response.currentParticipants()); // creator only
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void cancel_byCreator_shouldSetStatusCancelled() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);
        when(participationRepository.findByActivityId(1L)).thenReturn(List.of());

        activityService.cancel(1L, creator);

        assertEquals(ActivityStatus.CANCELLED, activity.getStatus());
    }

    @Test
    void cancel_byNonCreator_shouldThrowForbidden() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        assertThrows(ForbiddenException.class, () ->
                activityService.cancel(1L, otherUser));
    }

    @Test
    void cancel_alreadyCancelled_shouldThrowUnprocessable() {
        activity.setStatus(ActivityStatus.CANCELLED);
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        assertThrows(UnprocessableException.class, () ->
                activityService.cancel(1L, creator));
    }

    @Test
    void complete_shouldSetStatusCompleted() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Activity.class))).thenReturn(activity);
        when(participationRepository.findByActivityId(1L)).thenReturn(List.of());

        activityService.complete(1L, creator);

        assertEquals(ActivityStatus.COMPLETED, activity.getStatus());
    }

    @Test
    void delete_byNonCreator_shouldThrowForbidden() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        assertThrows(ForbiddenException.class, () ->
                activityService.delete(1L, otherUser));
    }
}
