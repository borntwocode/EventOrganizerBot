package uz.pdp.eventorganizerbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.eventorganizerbot.entity.Event;
import java.util.UUID;

public interface EventRepo extends JpaRepository<Event, UUID> {
}