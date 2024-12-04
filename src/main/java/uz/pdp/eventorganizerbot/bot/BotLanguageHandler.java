package uz.pdp.eventorganizerbot.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotLanguageHandler {

    public static String getUserLanguage(Message message) {
        if (message.from().languageCode() != null) {
            return message.from().languageCode();
        }
        return "en";
    }

    public static String getUserLanguage(CallbackQuery callbackQuery) {
        if (callbackQuery.from().languageCode() != null) {
            return callbackQuery.from().languageCode();
        }
        return "en";
    }

}
