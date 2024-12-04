package uz.pdp.eventorganizerbot.bot;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.messages.BotMessages;

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
                BotMessages.HELP.getMessage(languageCode)
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

}
