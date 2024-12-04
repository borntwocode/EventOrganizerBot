package uz.pdp.eventorganizerbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.eventorganizerbot.entity.RSVP;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RSVPRepo extends JpaRepository<RSVP, UUID> {

    @Query(value = "SELECT * FROM rsvp WHERE event_id = :eventId", nativeQuery = true)
    List<RSVP> findAllByEventId(UUID eventId);

    @Query(value = "select r.* from rsvp r where r.telegram_user_id = :userId and r.event_id = :eventId", nativeQuery = true)
    Optional<RSVP> findByUserIdAndEventId(UUID userId, UUID eventId);

}