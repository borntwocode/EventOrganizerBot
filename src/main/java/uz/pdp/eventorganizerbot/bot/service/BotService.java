package uz.pdp.eventorganizerbot.bot.service;

import uz.pdp.eventorganizerbot.entity.TelegramUser;

import java.util.UUID;

public interface BotService {

    void onStartCommand(TelegramUser user);

    void handleMenu(TelegramUser user, String text);

    void handleEventName(TelegramUser user, String text);

    void handleEventDate(TelegramUser user, String text);

    void handleEventVenue(TelegramUser user, String text);

    void handleEventDesc(TelegramUser user, String text);

    void handleEventMaxPart(TelegramUser user, String text);

    void handleEventOptions(TelegramUser user, String text);

    void handleRSVP(TelegramUser user, UUID eventId);

    void handleRSVPOptions(TelegramUser user, String data);

    void handleBackToMenu(TelegramUser user, String text);

    void handleEventMenu(TelegramUser user, String text);

    void handlePastEventDetails(TelegramUser user, String data);

    void handleBackToPastEvents(TelegramUser user, String text);

    void handleUpcomingEventDetails(TelegramUser user, String data);

    void handleUpcomingEventActions(TelegramUser user, String data);

    void handleEventInvitation(TelegramUser user, String data);

    void handleChangeLang(TelegramUser user, String data);

    void handleBackToUpcomingMenu(TelegramUser user, String text);

    void handlePastEventOptions(TelegramUser user, String text);

    void handleUpcomingEventOptions(TelegramUser user, String text);

}
