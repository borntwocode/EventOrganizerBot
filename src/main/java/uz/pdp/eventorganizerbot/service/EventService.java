package uz.pdp.eventorganizerbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.entity.Event;
import uz.pdp.eventorganizerbot.entity.RSVP;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import uz.pdp.eventorganizerbot.entity.enums.RSVPStatus;
import uz.pdp.eventorganizerbot.messages.BotMessages;
import uz.pdp.eventorganizerbot.repo.EventRepo;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    @Value("${bot.username}")
    private String botUsername;

    private final RSVPService rsvpService;
    private final EventRepo eventRepo;

    public String getMessage(TelegramUser user, String languageCode) {
        String message = BotMessages.EVENT_DETAILS.getMessage(languageCode);
        return message.formatted(
                user.getEventName(),
                user.getEventDate(),
                user.getEventVenue(),
                user.getEventDescription(),
                user.getEventMaxParticipants()
        );
    }

    public UUID createEvent(TelegramUser user) {
        Event event = Event.builder()
                .organizer(user)
                .title(user.getEventName())
                .eventDate(user.getEventDate())
                .venue(user.getEventVenue())
                .description(user.getEventDescription())
                .maxParticipants(user.getEventMaxParticipants())
                .build();
        return eventRepo.save(event).getId();
    }

    public String getEventMessage(Event event, String languageCode, TelegramUser user) {
        String message = BotMessages.EVENT_DETAILS_V2.getMessage(languageCode);
        return message.formatted(
                user.getFirstName(),
                event.getTitle(),
                event.getEventDate(),
                event.getVenue(),
                event.getDescription(),
                event.getMaxParticipants(),
                event.getCreatedAt().format(DateTimeFormatter.ISO_DATE)
        );
    }

    public String genInvLink(UUID eventId) {
        return "https://t.me/%s?start=%s".formatted(botUsername, eventId);
    }

    public Optional<Event> getEvent(UUID eventId) {
        return eventRepo.findById(eventId);
    }

    public String formatNotifMsg(String message, TelegramUser organizer, TelegramUser user, Event event) {
        List<RSVP> rsvps = rsvpService.findAllByEventId(event.getId());
        RSVP userRsvp = rsvps.stream()
                .filter(rsvp -> user.getId().equals(rsvp.getUser().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("RSVP not found for user"));
        EnumMap<RSVPStatus, Long> counts = rsvps.stream()
                .collect(Collectors.groupingBy(RSVP::getStatus, () -> new EnumMap<>(RSVPStatus.class), Collectors.counting()));
        long yesCount = counts.getOrDefault(RSVPStatus.YES, 0L);
        long noCount = counts.getOrDefault(RSVPStatus.NO, 0L);
        long maybeCount = counts.getOrDefault(RSVPStatus.MAYBE, 0L);
        return message.formatted(
                organizer.getFirstName(),
                user.getFirstName(),
                event.getTitle(),
                RSVPStatus.getRsvpResponse(userRsvp.getStatus()),
                event.getEventDate(),
                yesCount,
                noCount,
                maybeCount
        );
    }

}
