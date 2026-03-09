package com.whosup.repository;

import com.whosup.entity.Activity;
import com.whosup.entity.ActivityCategory;
import com.whosup.entity.ActivityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:category IS NULL OR a.category = :category) AND " +
           "(:search IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) " +
           "OR LOWER(a.description) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')))")
    Page<Activity> findWithFilters(
            @Param("status") ActivityStatus status,
            @Param("category") ActivityCategory category,
            @Param("search") String search,
            Pageable pageable);

    List<Activity> findByCreatorId(Long creatorId);

    @Modifying
    @Query("UPDATE Activity a SET a.status = com.whosup.entity.ActivityStatus.COMPLETED " +
           "WHERE a.activityDate < :now AND (a.status = com.whosup.entity.ActivityStatus.OPEN " +
           "OR a.status = com.whosup.entity.ActivityStatus.FULL)")
    int completeExpiredActivities(@Param("now") Instant now);
}
