package com.whosup.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "participations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "activity_id"})
})
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = Instant.now();
    }

    public Participation() {}

    public Participation(User user, Activity activity) {
        this.user = user;
        this.activity = activity;
    }

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Activity getActivity() { return activity; }
    public void setActivity(Activity activity) { this.activity = activity; }

    public Instant getJoinedAt() { return joinedAt; }
}
