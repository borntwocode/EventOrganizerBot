package uz.pdp.eventorganizerbot.bot.service;

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
import uz.pdp.eventorganizerbot.service.EventService;
import uz.pdp.eventorganizerbot.service.RSVPService;
import uz.pdp.eventorganizerbot.service.SendMsgService;
import uz.pdp.eventorganizerbot.service.TelegramUserService;

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
//            handleMyEvents(user);
        } else if (text.equals(BotMessages.INVITE_FRIENDS.getMessage(user.getLanguageCode()))) {
//            handleInviteFriends(user);
        } else if (text.equals(BotMessages.HELP.getMessage(user.getLanguageCode()))) {
            handleHelp(user);
        } else {
            onStartCommand(user);
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
            user.setEventDate(text);
            askEventVenue(user, languageCode);
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
        eventOptional.ifPresentOrElse(event -> {
            // Check if the user is not the organizer of the event
            if (!event.getOrganizer().getId().equals(user.getId())) {
                // Check if the user has already RSVP'd to the event
                boolean hasRSVPed = rsvpService.findAllByEventId(eventId).stream()
                        .anyMatch(rsvp -> rsvp.getUser().getId().equals(user.getId()));

                if (!hasRSVPed) {
                    // Create a new RSVP
                    UUID rsvpId = rsvpService.createRSVP(event, user);
                    String languageCode = user.getLanguageCode();
                    String message = eventService.getEventMessage(event, languageCode, user);
                    sendMsgService.sendWithButton(user, message, botUtils.createRSVPButtons(languageCode, event.getId()));
                    System.out.println("RSVP created successfully with ID: " + rsvpId);
                } else {
                    System.out.println("User has already RSVP'd to this event.");
                }
            } else {
                System.out.println("The user is the organizer and cannot RSVP to their own event.");
            }
        }, () -> System.out.println("Event with ID " + eventId + " not found."));
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
            }
        });
    }

    @Override
    public void handleBackToMenu(TelegramUser user, String text) {
        String languageCode = user.getLanguageCode();
        if(text.equals(BotMessages.BACK.getMessage(languageCode))){
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
