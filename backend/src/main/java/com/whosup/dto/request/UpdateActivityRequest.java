package com.whosup.dto.request;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record UpdateActivityRequest(
        @Size(min = 3, max = 200) String title,
        @Size(max = 2000) String description,
        @Size(max = 300) String location,
        @Future Instant activityDate,
        String category,
        @Min(2) @Max(100) Integer maxParticipants
) {}
