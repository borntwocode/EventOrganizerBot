package uz.pdp.eventorganizerbot.bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.entity.Event;
import uz.pdp.eventorganizerbot.messages.BotMessages;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BotUtils {

    public Keyboard createMenuButtons(String languageCode) {
        var keyboardMarkup = new ReplyKeyboardMarkup(
                BotMessages.CREATE_EVENT.getMessage(languageCode),
                BotMessages.MY_EVENTS.getMessage(languageCode)
        );
        keyboardMarkup.addRow(
                BotMessages.INVITE_FRIENDS.getMessage(languageCode),
                BotMessages.HELP.getMessage(languageCode)
        );
        keyboardMarkup.addRow(
                BotMessages.CHANGE_LANG.getMessage(languageCode)
        );
        return keyboardMarkup.resizeKeyboard(true).oneTimeKeyboard(true);
    }

    public Keyboard createBackButton(String languageCode) {
        var replyMarkup = new ReplyKeyboardMarkup(
                BotMessages.BACK.getMessage(languageCode)
        );
        return replyMarkup.oneTimeKeyboard(true).resizeKeyboard(true);
    }

    public Keyboard createBackSkipButtons(String languageCode) {
        var replyMarkup = new ReplyKeyboardMarkup(
                BotMessages.BACK.getMessage(languageCode),
                BotMessages.SKIP.getMessage(languageCode)
        );
        return replyMarkup.oneTimeKeyboard(true).resizeKeyboard(true);
    }

    public Keyboard createEventButtons(String languageCode) {
        var replyMarkup = new ReplyKeyboardMarkup(
                BotMessages.CONFIRM.getMessage(languageCode),
                BotMessages.EDIT.getMessage(languageCode),
                BotMessages.CANCEL.getMessage(languageCode)
        );
        return replyMarkup.oneTimeKeyboard(true).resizeKeyboard(true);
    }

    public Keyboard createRSVPButtons(String languageCode, UUID eventId) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        String yes = BotMessages.RSVP_YES.getMessage(languageCode);
        String no = BotMessages.RSVP_NO.getMessage(languageCode);
        String maybe = BotMessages.RSVP_MAYBE.getMessage(languageCode);
        keyboardMarkup.addRow(new InlineKeyboardButton(yes).callbackData("rsvp_+_" + eventId));
        keyboardMarkup.addRow(new InlineKeyboardButton(no).callbackData("rsvp_-_" + eventId));
        keyboardMarkup.addRow(new InlineKeyboardButton(maybe).callbackData("rsvp_|_" + eventId));
        return keyboardMarkup;
    }

    public Keyboard createEventMenuButtons(String languageCode) {
        var replyMarkup = new ReplyKeyboardMarkup(
                BotMessages.PAST_EVENTS.getMessage(languageCode),
                BotMessages.UPCOMING_EVENTS.getMessage(languageCode)
        );
        replyMarkup.addRow(BotMessages.BACK.getMessage(languageCode));
        return replyMarkup.oneTimeKeyboard(true).resizeKeyboard(true);
    }

    public Keyboard createUpcomingPastEventButtons(List<Event> events, String languageCode, String upcomingPast) {
        int maxButtonsPerRow = 5;
        int numRows = (int) Math.ceil((double) events.size() / maxButtonsPerRow);
        InlineKeyboardButton[][] buttons = new InlineKeyboardButton[numRows][];
        int buttonCount = 0;
        for (int i = 0; i < numRows; i++) {
            int buttonsInCurrentRow = Math.min(maxButtonsPerRow, events.size() - buttonCount);
            buttons[i] = new InlineKeyboardButton[buttonsInCurrentRow];
            for (int j = 0; j < buttonsInCurrentRow; j++) {
                Event event = events.get(buttonCount++);
                buttons[i][j] = new InlineKeyboardButton(String.valueOf(buttonCount)).callbackData(upcomingPast + "_" + event.getId());
            }
        }
        var back = new InlineKeyboardButton(BotMessages.BACK.getMessage(languageCode)).callbackData(upcomingPast + "_BACK");
        return new InlineKeyboardMarkup(buttons).addRow(back);
    }

    public Keyboard createUpcomingEventOrganizerButtons(Event event, String languageCode) {
        var keyboardMarkup = new InlineKeyboardMarkup();
        var cancel = new InlineKeyboardButton(BotMessages.CANCEL.getMessage(languageCode)).callbackData("EVENT_ORGANIZER_CANCEL_" + event.getId());
        var sendReminder = new InlineKeyboardButton(BotMessages.SEND_REMINDER.getMessage(languageCode)).callbackData("EVENT_ORGANIZER_REMINDER_" + event.getId());
        keyboardMarkup.addRow(cancel);
        keyboardMarkup.addRow(sendReminder);
        var back = new InlineKeyboardButton(BotMessages.BACK.getMessage(languageCode)).callbackData("EVENT_ORGANIZER_BACK");
        keyboardMarkup.addRow(back);
        return keyboardMarkup;
    }

    public Keyboard createLangButtons() {
        var keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.addRow(
                new InlineKeyboardButton("\uD83C\uDDFA\uD83C\uDDFFuz").callbackData("LANG_uz"),
                new InlineKeyboardButton("\uD83C\uDDF7\uD83C\uDDFAru").callbackData("LANG_ru"),
                new InlineKeyboardButton("\uD83C\uDDEC\uD83C\uDDE7en").callbackData("LANG_en")
        );
        return keyboardMarkup;
    }

}