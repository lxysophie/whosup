package com.whosup.controller;

import com.whosup.dto.response.ActivityResponse;
import com.whosup.entity.User;
import com.whosup.service.ActivityService;
import com.whosup.service.ParticipationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ParticipationController {

    private final ParticipationService participationService;
    private final ActivityService activityService;

    public ParticipationController(ParticipationService participationService,
                                    ActivityService activityService) {
        this.participationService = participationService;
        this.activityService = activityService;
    }

    @PostMapping("/activities/{id}/join")
    public ResponseEntity<Void> join(@PathVariable Long id, @AuthenticationPrincipal User user) {
        participationService.join(id, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/activities/{id}/leave")
    public ResponseEntity<Void> leave(@PathVariable Long id, @AuthenticationPrincipal User user) {
        participationService.leave(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/me/activities")
    public ResponseEntity<List<ActivityResponse>> myJoinedActivities(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(participationService.getJoinedActivities(user.getId()));
    }

    @GetMapping("/users/me/created")
    public ResponseEntity<List<ActivityResponse>> myCreatedActivities(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.findByCreator(user.getId()));
    }
}
