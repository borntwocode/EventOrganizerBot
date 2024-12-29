package uz.pdp.eventorganizerbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.entity.Event;
import uz.pdp.eventorganizerbot.entity.RSVP;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import uz.pdp.eventorganizerbot.repo.RSVPRepo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RSVPService {

    private final RSVPRepo rsvpRepo;

    public UUID createRSVP(Event event, TelegramUser user) {
        RSVP rsvp = RSVP.builder()
                .event(event)
                .user(user)
                .build();
        return rsvpRepo.save(rsvp).getId();
    }

    public Optional<RSVP> getUserRSVPByEventId(UUID userId, UUID eventId) {
        return rsvpRepo.findByUserIdAndEventId(userId, eventId);
    }

    public void save(RSVP rsvp) {
        rsvpRepo.save(rsvp);
    }

    public List<RSVP> findAllByEventId(UUID eventId) {
        return rsvpRepo.findAllByEventId(eventId);
    }

}
