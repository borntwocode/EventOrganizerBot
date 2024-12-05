package uz.pdp.eventorganizerbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.eventorganizerbot.entity.Event;
import java.util.List;
import java.util.UUID;

public interface EventRepo extends JpaRepository<Event, UUID> {

    @Query(value = "SELECT * FROM event e WHERE e.organizer_id = :userId AND e.event_date < CURRENT_DATE ORDER BY e.event_date DESC limit 10", nativeQuery = true)
    List<Event> findAllPastEventsByUserId(UUID userId);

    @Query(value = "SELECT * FROM event e WHERE e.organizer_id = :userId AND e.event_date >= CURRENT_DATE ORDER BY e.event_date ASC limit 10", nativeQuery = true)
    List<Event> findFirstUpcomingEventByOrganizerId(UUID userId);


}