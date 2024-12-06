package uz.pdp.eventorganizerbot.bot;

import com.pengrad.telegrambot.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.bot.service.BotService;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import uz.pdp.eventorganizerbot.entity.enums.TgState;
import uz.pdp.eventorganizerbot.messages.BotCommands;
import uz.pdp.eventorganizerbot.service.TelegramUserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final BotService botService;
    private final TelegramUserService userService;

    @Async
    public void handleUpdate(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
    }

    private void handleMessage(Message message) {
        String text = message.text();
        TelegramUser user = userService.findUser(message);
        if (text != null) {
            handleTextMessages(user, text);
        }
    }

    private void handleTextMessages(TelegramUser user, String text) {
        System.out.println(text);
        if (text.startsWith(BotCommands.START)) {
            String[] parts = text.split(" ", 2);
            if (parts.length > 1) {
                UUID eventId = UUID.fromString(parts[1]);
                botService.handleRSVP(user, eventId);
            } else {
                botService.onStartCommand(user);
            }
        } else {
            switch (user.getState()) {
                case CHOOSING_MENU -> botService.handleMenu(user, text);
                case CHOOSING_EVENT_MENU -> botService.handleEventMenu(user, text);
                case ENTERING_EVENT_NAME -> botService.handleEventName(user, text);
                case ENTERING_EVENT_DATE -> botService.handleEventDate(user, text);
                case ENTERING_EVENT_VENUE -> botService.handleEventVenue(user, text);
                case ENTERING_EVENT_DESC -> botService.handleEventDesc(user, text);
                case ENTERING_EVENT_MAX_PART -> botService.handleEventMaxPart(user, text);
                case CHOOSING_EVENT_OPTIONS -> botService.handleEventOptions(user, text);
                case GOING_BACK_TO_MAIN_MENU -> botService.handleBackToMenu(user, text);
                case GOING_BACK_TO_PAST_EVENTS -> botService.handleBackToPastEvents(user, text);
            }
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.data();
        TelegramUser user = userService.findUser(callbackQuery);
        if (data != null && data.startsWith("rsvp")) {
            botService.handleRSVPOptions(user, data);
        } else if (data != null && data.startsWith("PAST") && user.getState().equals(TgState.CHOOSING_PAST_EVENT)) {
            botService.handlePastEventDetails(user, data);
        } else if (data != null && data.startsWith("UPCOMING") && user.getState().equals(TgState.CHOOSING_UPCOMING_EVENT)) {
            botService.handleUpcomingEventDetails(user, data);
        } else if (data != null && data.startsWith("EVENT_ORGANIZER") && user.getState().equals(TgState.UPCOMING_EVENTS_ORGANIZER)) {
            botService.handleUpcomingEventActions(user, data);
        }
    }

}
