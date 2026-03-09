package com.whosup.service;

import com.whosup.entity.*;
import com.whosup.exception.ConflictException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipationServiceTest {

    @Mock
    private ParticipationRepository participationRepository;

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ParticipationService participationService;

    private User creator;
    private User joiner;
    private Activity activity;

    @BeforeEach
    void setUp() {
        creator = new User("creator@test.com", "password", "Creator");
        creator.setId(1L);

        joiner = new User("joiner@test.com", "password", "Joiner");
        joiner.setId(2L);

        activity = new Activity();
        activity.setId(1L);
        activity.setTitle("Test Activity");
        activity.setLocation("Test");
        activity.setActivityDate(Instant.now().plus(1, ChronoUnit.DAYS));
        activity.setMaxParticipants(4);
        activity.setStatus(ActivityStatus.OPEN);
        activity.setCreator(creator);
    }

    @Test
    void join_success() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(participationRepository.existsByUserIdAndActivityId(2L, 1L)).thenReturn(false);
        when(participationRepository.countByActivityId(1L)).thenReturn(0);

        participationService.join(1L, joiner);

        verify(participationRepository).save(any(Participation.class));
    }

    @Test
    void join_creatorSelfJoin_shouldThrowConflict() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        assertThrows(ConflictException.class, () ->
                participationService.join(1L, creator));
    }

    @Test
    void join_alreadyJoined_shouldThrowConflict() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(participationRepository.existsByUserIdAndActivityId(2L, 1L)).thenReturn(true);

        assertThrows(ConflictException.class, () ->
                participationService.join(1L, joiner));
    }

    @Test
    void join_cancelledActivity_shouldThrowUnprocessable() {
        activity.setStatus(ActivityStatus.CANCELLED);
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        assertThrows(UnprocessableException.class, () ->
                participationService.join(1L, joiner));
    }

    @Test
    void join_fullActivity_shouldThrowUnprocessable() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(participationRepository.existsByUserIdAndActivityId(2L, 1L)).thenReturn(false);
        when(participationRepository.countByActivityId(1L)).thenReturn(3); // 3 + 1 creator = 4 = max

        assertThrows(UnprocessableException.class, () ->
                participationService.join(1L, joiner));
    }

    @Test
    void join_shouldAutoTransitionToFull() {
        activity.setMaxParticipants(3); // creator + 1 more = capacity at 2 joined
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(participationRepository.existsByUserIdAndActivityId(2L, 1L)).thenReturn(false);
        when(participationRepository.countByActivityId(1L)).thenReturn(1); // 1 + 1 creator = 2, capacity at 3

        participationService.join(1L, joiner);

        assertEquals(ActivityStatus.FULL, activity.getStatus());
        verify(activityRepository).save(activity);
    }

    @Test
    void leave_shouldAutoTransitionToOpen() {
        activity.setStatus(ActivityStatus.FULL);
        Participation participation = new Participation(joiner, activity);

        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(participationRepository.findByUserIdAndActivityId(2L, 1L))
                .thenReturn(Optional.of(participation));

        participationService.leave(1L, joiner);

        assertEquals(ActivityStatus.OPEN, activity.getStatus());
        verify(participationRepository).delete(participation);
    }

    @Test
    void leave_notParticipant_shouldThrowConflict() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(participationRepository.findByUserIdAndActivityId(2L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ConflictException.class, () ->
                participationService.leave(1L, joiner));
    }
}
