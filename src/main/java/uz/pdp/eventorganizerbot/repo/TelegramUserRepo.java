package uz.pdp.eventorganizerbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import java.util.Optional;
import java.util.UUID;

public interface TelegramUserRepo extends JpaRepository<TelegramUser, UUID> {

    Optional<TelegramUser> findByChatId(Long chatId);

}