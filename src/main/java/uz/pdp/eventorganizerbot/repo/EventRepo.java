package uz.pdp.eventorganizerbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.eventorganizerbot.entity.Event;
import java.util.List;
import java.util.UUID;

public interface EventRepo extends JpaRepository<Event, UUID> {

    @Query(value = """
            SELECT e.* FROM event e LEFT JOIN rsvp r ON r.event_id = e.id
            WHERE (e.organizer_id = :userId OR r.telegram_user_id = :userId) AND e.event_date <= CURRENT_TIMESTAMP
            ORDER BY e.event_date DESC LIMIT 10
            """, nativeQuery = true)
    List<Event> findAllPastEventsByUserId(UUID userId);

    @Query(value = """
            SELECT e.* FROM event e LEFT JOIN rsvp r ON r.event_id = e.id
            WHERE (e.organizer_id = :userId OR r.telegram_user_id = :userId) AND e.event_date > CURRENT_TIMESTAMP
            ORDER BY e.event_date
            """, nativeQuery = true)
    List<Event> findAllUpcomingEventsByUserId(UUID userId);

    @Query(value = """
            SELECT e.* FROM event e LEFT JOIN rsvp r ON r.event_id = e.id
            WHERE e.organizer_id = :userId AND e.event_date > CURRENT_TIMESTAMP
            ORDER BY e.event_date
            """, nativeQuery = true)
    List<Event> findAllUpcomingEventsByOrganizerId(UUID userId);

}