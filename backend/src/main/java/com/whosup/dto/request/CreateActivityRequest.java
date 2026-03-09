package com.whosup.dto.request;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record CreateActivityRequest(
        @NotBlank @Size(min = 3, max = 200) String title,
        @Size(max = 2000) String description,
        @NotBlank @Size(max = 300) String location,
        @NotNull @Future Instant activityDate,
        @NotNull String category,
        @Min(2) @Max(100) int maxParticipants
) {}
