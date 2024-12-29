package uz.pdp.eventorganizerbot.service;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.bot.BotLanguageHandler;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import uz.pdp.eventorganizerbot.entity.enums.TgState;
import uz.pdp.eventorganizerbot.repo.TelegramUserRepo;

@Service
@RequiredArgsConstructor
public class TelegramUserService {

    private final TelegramUserRepo telegramUserRepo;

    public TelegramUser findUser(Message message) {
        Long chatId = message.chat().id();
        String username = message.from().username();
        String firstName = message.from().firstName();
        String userLanguage = BotLanguageHandler.getUserLanguage(message);
        return telegramUserRepo.findByChatId(chatId)
                .orElseGet(() -> createAndSaveUser(chatId, username, firstName, userLanguage));
    }

    private TelegramUser createAndSaveUser(Long chatId, String username, String firstName, String userLanguage) {
        TelegramUser user = TelegramUser.builder()
                .chatId(chatId)
                .username(username)
                .firstName(firstName)
                .languageCode(userLanguage)
                .state(TgState.START)
                .build();
        return telegramUserRepo.save(user);
    }

    public TelegramUser findUser(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.from().id();
        String username = callbackQuery.from().username();
        String firstName = callbackQuery.from().firstName();
        String userLanguage = BotLanguageHandler.getUserLanguage(callbackQuery);
        return telegramUserRepo.findByChatId(chatId)
                .orElseGet(() -> createAndSaveUser(chatId, firstName, username, userLanguage));
    }

    public void changeUserState(TelegramUser user, TgState state) {
        user.setState(state);
        telegramUserRepo.save(user);
    }

    public void save(TelegramUser user) {
        telegramUserRepo.save(user);
    }

    public void changeUserLang(TelegramUser user, String lang) {
        user.setLanguageCode(lang);
        telegramUserRepo.save(user);
    }

}
