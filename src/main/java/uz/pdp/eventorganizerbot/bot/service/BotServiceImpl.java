package uz.pdp.eventorganizerbot.bot.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.bot.BotUtils;
import uz.pdp.eventorganizerbot.entity.Event;
import uz.pdp.eventorganizerbot.entity.RSVP;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import uz.pdp.eventorganizerbot.entity.enums.RSVPStatus;
import uz.pdp.eventorganizerbot.entity.enums.TgState;
import uz.pdp.eventorganizerbot.messages.BotMessages;
import uz.pdp.eventorganizerbot.service.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final SendMsgService sendMsgService;
    private final TelegramUserService userService;
    private final BotUtils botUtils;
    private final EventService eventService;
    private final RSVPService rsvpService;
    private final TelegramUserService telegramUserService;
    private final TestService testService;

    @Override
    public void onStartCommand(TelegramUser user) {
        String languageCode = user.getLanguageCode();
        String message = BotMessages.CHOOSE_MENU.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createMenuButtons(languageCode));
        userService.changeUserState(user, TgState.CHOOSING_MENU);
    }

    @Override
    public void handleMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.CREATE_EVENT.getMessage(user.getLanguageCode()))) {
            handleCreateEvent(user);
        } else if (text.equals(BotMessages.MY_EVENTS.getMessage(user.getLanguageCode()))) {
            handleMyEvents(user);
        } else if (text.equals(BotMessages.INVITE_FRIENDS.getMessage(user.getLanguageCode()))) {
            handleInviteFriends(user);
        } else if (text.equals(BotMessages.CHANGE_LANG.getMessage(user.getLanguageCode()))) {
            handleLang(user);
        } else if (text.equals(BotMessages.HELP.getMessage(user.getLanguageCode()))) {
            handleHelp(user);
        } else {
            onStartCommand(user);
        }
    }

    private void handleLang(TelegramUser user) {
        String languageCode = user.getLanguageCode();
        sendMsgService.sendWithButton(user, BotMessages.SELECT_LANG.getMessage(languageCode), botUtils.createLangButtons());
        userService.changeUserState(user, TgState.CHANGING_LANG);
    }

    private void handleInviteFriends(TelegramUser user) {
        List<Event> events = eventService.getUpcomingEventsByOrganizer(user);
        String languageCode = user.getLanguageCode();
        if (events.isEmpty()) {
            String message = BotMessages.NO_EVENTS.getMessage(languageCode);
            sendMsgService.sendWithButton(user, message, botUtils.createBackButton(languageCode));
        } else {
            String upcomingEventMessage = eventService.getUpcomingEventMessage(events, languageCode);
            sendMsgService.sendWithButton(user, upcomingEventMessage, botUtils.createUpcomingPastEventButtons(events, languageCode, "UPCOMING"));
            userService.changeUserState(user, TgState.CHOOSING_EVENT_INVITATION);
        }
    }

    private void handleMyEvents(TelegramUser user) {
        String languageCode = user.getLanguageCode();
        String message = BotMessages.CHOOSE_MENU.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createEventMenuButtons(languageCode));
        userService.changeUserState(user, TgState.CHOOSING_EVENT_MENU);
    }

    @Override
    public void handleEventMenu(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.PAST_EVENTS.getMessage(languageCode))) {
            handlePastEvents(user);
        } else if (text.equals(BotMessages.UPCOMING_EVENTS.getMessage(languageCode))) {
            handleUpcomingEvents(user);
        } else if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            onStartCommand(user);
        } else {
            handleMyEvents(user);
        }
    }

    private void handleUpcomingEvents(TelegramUser user) {
        List<Event> events = eventService.getUpcomingEvents(user);
        String languageCode = user.getLanguageCode();
        if (events.isEmpty()) {
            String message = BotMessages.NO_UPCOMING_EVENTS.getMessage(languageCode);
            sendMsgService.sendWithButton(user, message, botUtils.createBackButton(languageCode));
            userService.changeUserState(user, TgState.CHOOSING_UPCOMING_EVENT);
        } else {
            String upcomingEventMessage = eventService.getUpcomingEventMessage(events, languageCode);
            sendMsgService.sendWithButton(user, upcomingEventMessage, botUtils.createUpcomingPastEventButtons(events, languageCode, "UPCOMING"));
            userService.changeUserState(user, TgState.CHOOSING_UPCOMING_EVENT);
        }
    }

    @Override
    public void handlePastEventDetails(TelegramUser user, String data) {
        String languageCode = user.getLanguageCode();
        String payload = data.split("_")[1];
        if (payload.equals("BACK")) {
            handleMyEvents(user);
        } else {
            UUID eventId = UUID.fromString(payload);
            Optional<Event> eventOpt = eventService.getEvent(eventId);
            eventOpt.ifPresent(event -> {
                String eventMessage;
                if (user.getId().equals(event.getOrganizer().getId())) {
                    eventMessage = eventService.getPastUpcomingEventDetailsMessageOrganizer(event, languageCode);
                } else {
                    eventMessage = eventService.getPastUpcomingEventDetailsMessageAttendee(event, languageCode);
                }
                sendMsgService.sendWithButton(user, eventMessage, botUtils.createBackButton(languageCode));
                userService.changeUserState(user, TgState.GOING_BACK_TO_PAST_EVENTS);
            });
        }
    }

    @Override
    public void handleBackToPastEvents(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user.getLanguageCode()))) {
            handlePastEvents(user);
        }
    }

    @Override
    public void handleUpcomingEventDetails(TelegramUser user, String data) {
        String languageCode = user.getLanguageCode();
        String payload = data.split("_")[1];
        if (payload.equals("BACK")) {
            handleMyEvents(user);
        } else {
            UUID eventId = UUID.fromString(payload);
            Optional<Event> eventOpt = eventService.getEvent(eventId);
            eventOpt.ifPresent(event -> {
                if (user.getId().equals(event.getOrganizer().getId())) {
                    String eventMessage = eventService.getPastUpcomingEventDetailsMessageOrganizer(event, languageCode);
                    sendMsgService.sendWithButton(user, eventMessage, botUtils.createUpcomingEventOrganizerButtons(event, languageCode));
                    userService.changeUserState(user, TgState.UPCOMING_EVENTS_ORGANIZER);
                } else {
                    String eventMessage = eventService.getPastUpcomingEventDetailsMessageAttendee(event, languageCode);
                    sendMsgService.sendWithButton(user, eventMessage, botUtils.createBackButton(languageCode));
                    userService.changeUserState(user, TgState.GOING_BACK_TO_UPCOMING_MENU);
                }
            });
        }
    }

    @Transactional
    @Override
    public void handleUpcomingEventActions(TelegramUser user, String data) {
        String[] payload = data.split("_");
        String action = payload[2];
        if (action.equals("BACK")) {
            handleUpcomingEvents(user);
        } else {
            String languageCode = user.getLanguageCode();
            UUID eventId = UUID.fromString(payload[3]);
            eventService.getEvent(eventId).ifPresent(event -> {
                if (event.getEventDateTime().isBefore(LocalDateTime.now()) ||
                    event.getEventDateTime().isBefore(LocalDateTime.now().plusMinutes(5))) {
                    sendMsgService.sendMessage(user, BotMessages.EVENT_DEADLINE_PASSED.getMessage(languageCode));
                    return;
                }
                if (action.equals("CANCEL")) {
                    handleCancelEvent(user, languageCode, eventId);
                } else if (action.equals("REMINDER")) {
                    handleSendReminderEventAttendees(user, languageCode, eventId);
                }
            });
        }
    }

    @Override
    public void handleEventInvitation(TelegramUser user, String data) {
        String languageCode = user.getLanguageCode();
        String payload = data.split("_")[1];
        if (payload.equals("BACK")) {
            onStartCommand(user);
        } else {
            UUID eventId = UUID.fromString(payload);
            Optional<Event> eventOpt = eventService.getEvent(eventId);
            eventOpt.ifPresent(event -> {
                if (event.getEventDateTime().isAfter(LocalDateTime.now())) {
                    String invLink = eventService.genInvLink(eventId);
                    sendMsgService.sendWithButton(user, invLink, botUtils.createBackButton(languageCode));
                    userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
                }
            });
        }
    }

    @Override
    public void handleChangeLang(TelegramUser user, String data) {
        String lang = data.split("_")[1];
        userService.changeUserLang(user, lang);
        onStartCommand(user);
    }

    @Override
    public void handleBackToUpcomingMenu(TelegramUser user, String text) {
        if (text.equals(BotMessages.BACK.getMessage(user.getLanguageCode()))) {
            handleUpcomingEvents(user);
        }
    }

    @Override
    public void handlePastEventOptions(TelegramUser user, String text) {
        if(text.equals(BotMessages.BACK.getMessage(user.getLanguageCode()))) {
            handleMyEvents(user);
        }
    }

    @Override
    public void handleUpcomingEventOptions(TelegramUser user, String text) {
        if(text.equals(BotMessages.BACK.getMessage(user.getLanguageCode()))) {
            handleMyEvents(user);
        }
    }

    private void handleSendReminderEventAttendees(TelegramUser user, String languageCode, UUID eventId) {
        List<RSVP> rsvps = rsvpService.findAllByEventId(eventId);
        if (rsvps.isEmpty()) {
            sendMsgService.sendMessage(user, BotMessages.NO_RSVPS.getMessage(languageCode));
        } else {
            eventService.sendReminderToAttendees(rsvps);
        }
    }

    private void handleCancelEvent(TelegramUser user, String languageCode, UUID eventId) {
        String message = BotMessages.EVENT_CANCELLED.getMessage(languageCode);
        eventService.getEvent(eventId).ifPresent(event -> {
            List<RSVP> rsvps = rsvpService.findAllByEventId(eventId);
            eventService.deleteById(eventId);
            sendMsgService.sendMessage(user, message);
            eventService.notifyAttendees(event, rsvps);
            handleUpcomingEvents(user);
        });
    }

    private void handlePastEvents(TelegramUser user) {
        List<Event> events = eventService.getPastEvents(user);
        String languageCode = user.getLanguageCode();
        if (events.isEmpty()) {
            String message = BotMessages.NO_PAST_EVENTS.getMessage(languageCode);
            sendMsgService.sendWithButton(user, message, botUtils.createBackButton(languageCode));
            userService.changeUserState(user, TgState.CHOOSING_PAST_EVENT);
        } else {
            String pastEventMessage = eventService.getPastEventMessage(events, languageCode);
            sendMsgService.sendWithButton(user, pastEventMessage, botUtils.createUpcomingPastEventButtons(events, languageCode, "PAST"));
            userService.changeUserState(user, TgState.CHOOSING_PAST_EVENT);
        }
    }

    private void handleHelp(TelegramUser user) {
        String languageCode = user.getLanguageCode();
        String message = BotMessages.HELP_MESSAGE.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createBackButton(languageCode));
        userService.changeUserState(user, TgState.GOING_BACK_TO_MAIN_MENU);
    }

    @SneakyThrows
    private void handleCreateEvent(TelegramUser user) {
        String languageCode = user.getLanguageCode();
        String message = BotMessages.CREATE_EVENT_MESSAGE.getMessage(languageCode);
        sendMsgService.sendMessage(user, message);
        {
            Thread.sleep(1500);
            askEventName(user, languageCode);
        }
//        testService.createEvents(user);
    }

    private void askEventName(TelegramUser user, String languageCode) {
        String text = BotMessages.EVENT_NAME.getMessage(languageCode);
        sendMsgService.sendWithButton(user, text, botUtils.createBackButton(languageCode));
        userService.changeUserState(user, TgState.ENTERING_EVENT_NAME);
    }

    @Override
    public void handleEventName(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            onStartCommand(user);
        } else {
            user.setEventName(text);
            askEventDateTime(user, languageCode);
        }
    }

    private void askEventDateTime(TelegramUser user, String languageCode) {
        String message = BotMessages.EVENT_DATE_TIME.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createBackButton(languageCode));
        userService.changeUserState(user, TgState.ENTERING_EVENT_DATE);
    }

    @Override
    public void handleEventDate(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            askEventName(user, languageCode);
        } else {
            String expectedDateTimeFormat = "dd.MM.yyyy HH:mm";
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(expectedDateTimeFormat);
            try {
                LocalDateTime dateTime = LocalDateTime.parse(text, dateTimeFormatter);
                if (dateTime.isBefore(LocalDateTime.now().plusHours(1))) {
                    sendMsgService.sendMessage(user, BotMessages.EVENT_DATE_PASSED.getMessage(languageCode));
                } else {
                    user.setEventDateTime(dateTime);
                    askEventVenue(user, languageCode);
                }
            } catch (DateTimeParseException e) {
                String errorMessage = BotMessages.INVALID_DATE_TIME.getMessage(languageCode);
                sendMsgService.sendWithButton(user, errorMessage, botUtils.createBackButton(languageCode));
            }
        }
    }

    private void askEventVenue(TelegramUser user, String languageCode) {
        String message = BotMessages.EVENT_VENUE.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createBackButton(languageCode));
        userService.changeUserState(user, TgState.ENTERING_EVENT_VENUE);
    }

    @Override
    public void handleEventVenue(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            askEventDateTime(user, languageCode);
        } else {
            user.setEventVenue(text);
            askEventDescription(user, languageCode);
        }
    }

    private void askEventDescription(TelegramUser user, String languageCode) {
        String message = BotMessages.EVENT_DESCRIPTION.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createBackSkipButtons(languageCode));
        userService.changeUserState(user, TgState.ENTERING_EVENT_DESC);
    }

    @Override
    public void handleEventDesc(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            askEventVenue(user, languageCode);
        } else {
            if (text.equals(BotMessages.SKIP.getMessage(languageCode))) {
                user.setEventDescription(BotMessages.getSkipped(languageCode));
            } else {
                user.setEventDescription(text);
            }
            askEventMaxParticipants(user, languageCode);
        }
    }

    private void askEventMaxParticipants(TelegramUser user, String languageCode) {
        String message = BotMessages.EVENT_MAX_PARTICIPANTS.getMessage(languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createBackSkipButtons(languageCode));
        userService.changeUserState(user, TgState.ENTERING_EVENT_MAX_PART);
    }

    @Override
    public void handleEventMaxPart(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            askEventDescription(user, languageCode);
        } else {
            if (text.equals(BotMessages.SKIP.getMessage(languageCode))) {
                user.setEventMaxParticipants(BotMessages.getSkipped(languageCode));
            } else {
                user.setEventMaxParticipants(text);
            }
            telegramUserService.save(user);
            askEventOptions(user, languageCode);
        }
    }

    private void askEventOptions(TelegramUser user, String languageCode) {
        String message = eventService.getMessage(user, languageCode);
        sendMsgService.sendWithButton(user, message, botUtils.createEventButtons(languageCode));
        userService.changeUserState(user, TgState.CHOOSING_EVENT_OPTIONS);
    }

    @Override
    public void handleEventOptions(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.CONFIRM.getMessage(languageCode))) {
            UUID eventId = eventService.createEvent(user);
            String message = BotMessages.EVENT_CONFIRMATION.getMessage(languageCode);
            String linkedMessage = message + "\n\n\n" + eventService.genInvLink(eventId);
            sendMsgService.sendMessage(user, linkedMessage);
            onStartCommand(user);
        } else if (text.equals(BotMessages.EDIT.getMessage(languageCode))) {
            askEventMaxParticipants(user, languageCode);
        } else if (text.equals(BotMessages.CANCEL.getMessage(languageCode))) {
            onStartCommand(user);
        } else {
            askEventOptions(user, languageCode);
        }
    }

    @Override
    public void handleRSVP(TelegramUser user, UUID eventId) {
        Optional<Event> eventOptional = eventService.getEvent(eventId);
        String languageCode = user.getLanguageCode();
        eventOptional.ifPresentOrElse(event -> {
            if (!event.getOrganizer().getId().equals(user.getId())) {
                boolean hasRSVPed = rsvpService.findAllByEventId(eventId).stream()
                        .anyMatch(rsvp -> rsvp.getUser().getId().equals(user.getId()));

                if (!hasRSVPed) {

                    if (event.getEventDateTime().isBefore(LocalDateTime.now()) ||
                        event.getEventDateTime().isBefore(LocalDateTime.now().plusMinutes(5))) {
                        sendMsgService.sendMessage(user, BotMessages.EVENT_INV_DEADLINE_PASSED.getMessage(languageCode));
                    } else {
                        String message = eventService.getEventMessage(event, languageCode, user);
                        sendMsgService.sendWithButton(user, message, botUtils.createRSVPButtons(languageCode, event.getId()));
                    }
                } else {
                    sendMsgService.sendMessage(user, BotMessages.ALREADY_RSVPED.getMessage(languageCode));
                }
            } else {
                sendMsgService.sendMessage(user, BotMessages.SELF_EVENT.getMessage(languageCode));
            }
        }, () -> sendMsgService.sendMessage(user, BotMessages.EVENT_NO_LONGER_EXISTS.getMessage(languageCode)));
    }

    @Override
    public void handleRSVPOptions(TelegramUser user, String data) {
        String[] payload = data.split("_");
        String status = payload[1];
        UUID eventId = UUID.fromString(payload[2]);
        Optional<RSVP> userRSVP = rsvpService.getUserRSVPByEventId(user.getId(), eventId);
        userRSVP.ifPresent(rsvp -> {
            if (rsvp.getStatus() == null) {
                rsvp.setStatus(RSVPStatus.getFromString(status));
                rsvpService.save(rsvp);
                notifyOrganizer(eventId, user);
                String message = BotMessages.ANSWERED_TO_RSVP.getMessage(user.getLanguageCode())
                        .formatted(RSVPStatus.getRsvpResponse(rsvp.getStatus()));
                sendMsgService.sendMessage(user, message);
            }
        });
    }

    @Override
    public void handleBackToMenu(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if (text.equals(BotMessages.BACK.getMessage(languageCode))) {
            onStartCommand(user);
        }
    }

    private void notifyOrganizer(UUID eventId, TelegramUser user) {
        eventService.getEvent(eventId).ifPresent(event -> {
            TelegramUser organizer = event.getOrganizer();
            String message = BotMessages.NOTIFY_ORGANIZER.getMessage(user.getLanguageCode());
            String formattedMessage = eventService.formatNotifMsg(message, organizer, user, event);
            sendMsgService.sendMessage(organizer, formattedMessage);
        });
    }

}
