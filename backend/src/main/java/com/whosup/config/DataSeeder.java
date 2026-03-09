package com.whosup.config;

import com.whosup.entity.*;
import com.whosup.repository.ActivityRepository;
import com.whosup.repository.ParticipationRepository;
import com.whosup.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true")
public class DataSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ParticipationRepository participationRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public DataSeeder(UserRepository userRepository, ActivityRepository activityRepository,
                      ParticipationRepository participationRepository, PasswordEncoder passwordEncoder,
                      EntityManager entityManager) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.participationRepository = participationRepository;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        participationRepository.deleteAllInBatch();
        activityRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        entityManager.flush();
        seed();
    }

    private void seed() {
        String hashedPassword = passwordEncoder.encode("demo1234");

        User demo = userRepository.save(new User("demo@whosup.com", hashedPassword, "Demo User"));
        User alice = userRepository.save(new User("alice@example.com", hashedPassword, "Alice Chen"));

        Instant now = Instant.now();

        Activity a1 = createActivity("Watch Zootopia 2 at AMC",
                "Let's go watch Zootopia 2 together! I'll grab the tickets, just Venmo me later.",
                "AMC Empire 25, Times Square",
                now.plus(2, ChronoUnit.DAYS), ActivityCategory.MOVIES, 4, demo);

        Activity a2 = createActivity("Morning Tennis Match",
                "Looking for a doubles partner. Intermediate level preferred. I'll bring extra rackets.",
                "Central Park Tennis Center",
                now.plus(1, ChronoUnit.DAYS), ActivityCategory.SPORTS, 4, alice);

        createActivity("Study Session - Data Structures",
                "Prepping for midterms. Let's work through practice problems together.",
                "Butler Library, Columbia University",
                now.plus(3, ChronoUnit.DAYS), ActivityCategory.STUDY, 6, demo);

        createActivity("Dumpling Crawl in Flushing",
                "Hitting up 3 spots on Main Street. Bring your appetite!",
                "Flushing, Queens",
                now.plus(5, ChronoUnit.DAYS), ActivityCategory.DINING, 5, alice);

        createActivity("Sunset Hike at Bear Mountain",
                "Moderate 4-mile loop with Hudson Valley views. Meet at the parking lot.",
                "Bear Mountain State Park, NY",
                now.plus(4, ChronoUnit.DAYS), ActivityCategory.OUTDOORS, 8, demo);

        createActivity("Board Game Night in Williamsburg",
                "Bringing Catan, Ticket to Ride, and Codenames. Snacks provided!",
                "Hex & Co, Brooklyn",
                now.plus(6, ChronoUnit.DAYS), ActivityCategory.GAMING, 8, alice);

        participationRepository.save(new Participation(alice, a1));
        participationRepository.save(new Participation(demo, a2));
    }

    private Activity createActivity(String title, String description, String location,
                                     Instant date, ActivityCategory category,
                                     int maxParticipants, User creator) {
        Activity activity = new Activity();
        activity.setTitle(title);
        activity.setDescription(description);
        activity.setLocation(location);
        activity.setActivityDate(date);
        activity.setCategory(category);
        activity.setMaxParticipants(maxParticipants);
        activity.setCreator(creator);
        return activityRepository.save(activity);
    }
}
