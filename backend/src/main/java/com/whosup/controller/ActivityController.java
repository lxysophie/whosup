package com.whosup.controller;

import com.whosup.dto.request.CreateActivityRequest;
import com.whosup.dto.request.UpdateActivityRequest;
import com.whosup.dto.response.ActivityDetailResponse;
import com.whosup.dto.response.ActivityResponse;
import com.whosup.entity.ActivityCategory;
import com.whosup.entity.ActivityStatus;
import com.whosup.entity.User;
import com.whosup.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<Page<ActivityResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {

        size = Math.min(size, 50);
        ActivityStatus statusEnum = status != null ? ActivityStatus.valueOf(status) : null;
        ActivityCategory categoryEnum = category != null ? ActivityCategory.valueOf(category) : null;

        PageRequest pageable = PageRequest.of(page, size, Sort.by("activityDate").ascending());
        return ResponseEntity.ok(activityService.findAll(statusEnum, categoryEnum, search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDetailResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ActivityDetailResponse> create(
            @Valid @RequestBody CreateActivityRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(request, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityDetailResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateActivityRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        activityService.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ActivityDetailResponse> cancel(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.cancel(id, user));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ActivityDetailResponse> complete(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(activityService.complete(id, user));
    }
}
