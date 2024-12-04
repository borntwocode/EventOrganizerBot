package uz.pdp.eventorganizerbot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.entity.TelegramUser;

@Service
@RequiredArgsConstructor
public class SendMsgService {

    private final TelegramBot telegramBot;

    public void sendWithButton(TelegramUser user, String text, Keyboard buttons) {
        SendMessage message = new SendMessage(user.getChatId(), text);
        message.replyMarkup(buttons);
        telegramBot.execute(message);
    }

    public void sendMessage(TelegramUser user, String text) {
        SendMessage message = new SendMessage(user.getChatId(), text);
        telegramBot.execute(message);
    }

}
