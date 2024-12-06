package uz.pdp.eventorganizerbot.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

    private final SendMsgService sendMsgService;
    @Value("${bot.username}")
    private String botUsername;

    private final RSVPService rsvpService;
    private final EventRepo eventRepo;

    public String getMessage(TelegramUser user, String languageCode) {
        String message = BotMessages.EVENT_DETAILS.getMessage(languageCode);
        return message.formatted(
                user.getEventName(),
                user.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                user.getEventVenue(),
                user.getEventDescription(),
                user.getEventMaxParticipants()
        );
    }

    public UUID createEvent(TelegramUser user) {
        Event event = Event.builder()
                .organizer(user)
                .title(user.getEventName())
                .eventDateTime(user.getEventDateTime())
                .venue(user.getEventVenue())
                .description(user.getEventDescription())
                .maxParticipants(user.getEventMaxParticipants())
                .build();
        return eventRepo.save(event).getId();
    }

    public String getEventMessage(Event event, String languageCode, TelegramUser user) {
        String message = BotMessages.EVENT_DETAILS_INVITATION.getMessage(languageCode);
        return message.formatted(
                user.getFirstName(),
                event.getTitle(),
                event.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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
                event.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                yesCount,
                noCount,
                maybeCount
        );
    }

    public List<Event> getPastEvents(TelegramUser user) {
        return eventRepo.findAllPastEventsByUserId(user.getId());
    }

    public String getPastEventMessage(List<Event> events, String languageCode) {
        String eventDetailsTemplate = BotMessages.PAST_UPCOMING_EVENT_DETAILS.getMessage(languageCode);
        StringBuilder formattedMessage = new StringBuilder();
        formattedMessage.append(BotMessages.PAST_EVENTS.getMessage(languageCode)).append("\n\n");
        return getMessageBody(events, eventDetailsTemplate, formattedMessage);
    }

    public String getPastUpcomingEventDetailsMessageOrganizer(Event event, String languageCode) {
        String message = BotMessages.EVENT_DETAILS_ORGANIZER.getMessage(languageCode);
        List<RSVP> rsvps = rsvpService.findAllByEventId(event.getId());
        long count = rsvps.stream()
                .filter(rsvp -> rsvp.getStatus().equals(RSVPStatus.YES))
                .count();
        return message.formatted(
                event.getTitle(),
                event.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                event.getVenue(),
                event.getDescription(),
                event.getMaxParticipants(),
                count
        );
    }

    public String getPastUpcomingEventDetailsMessageAttendee(Event event, String languageCode) {
        String message = BotMessages.EVENT_DETAILS_ATTENDEE.getMessage(languageCode);
        return message.formatted(
                event.getTitle(),
                event.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                event.getVenue(),
                event.getDescription(),
                event.getMaxParticipants(),
                event.getOrganizer().getFirstName()
        );
    }

    public List<Event> getUpcomingEvents(TelegramUser user) {
        return eventRepo.findAllUpcomingEventsByUserId(user.getId());
    }

    public String getUpcomingEventMessage(List<Event> events, String languageCode) {
        String eventDetailsTemplate = BotMessages.PAST_UPCOMING_EVENT_DETAILS.getMessage(languageCode);
        StringBuilder formattedMessage = new StringBuilder();
        formattedMessage.append(BotMessages.UPCOMING_EVENTS.getMessage(languageCode)).append("\n\n");
        return getMessageBody(events, eventDetailsTemplate, formattedMessage);
    }

    @NotNull
    private String getMessageBody(List<Event> events, String eventDetailsTemplate, StringBuilder formattedMessage) {
        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            String eventName = event.getTitle();
            String eventDateTime = event.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            formattedMessage.append(String.format(eventDetailsTemplate,
                            i + 1,
                            eventName,
                            eventDateTime))
                    .append("\n\n");
        }
        return formattedMessage.toString();
    }

    public void deleteById(UUID eventId) {
        eventRepo.deleteById(eventId);
    }

    public void notifyAttendees(@NotNull Event event, List<RSVP> rsvps) {
        if(!rsvps.isEmpty()) {
            for (RSVP rsvp : rsvps) {
                TelegramUser user = rsvp.getUser();
                String message = getCancelledEventMessage(event, user);
                sendMsgService.sendMessage(user, message);
            }
        }
    }

    private String getCancelledEventMessage(Event event, TelegramUser user) {
        String message = BotMessages.CANCELLED_NOTIF_MESSAGE.getMessage(user.getLanguageCode());
        return message.formatted(
                user.getFirstName(),
                event.getTitle(),
                event.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                event.getVenue()
        );
    }

}
