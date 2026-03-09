package com.whosup.repository;

import com.whosup.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByUserIdAndActivityId(Long userId, Long activityId);
    boolean existsByUserIdAndActivityId(Long userId, Long activityId);
    int countByActivityId(Long activityId);
    List<Participation> findByActivityId(Long activityId);
    List<Participation> findByUserId(Long userId);
}
