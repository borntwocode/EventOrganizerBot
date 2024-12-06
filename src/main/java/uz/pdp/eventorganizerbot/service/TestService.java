package uz.pdp.eventorganizerbot.service;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.eventorganizerbot.entity.Event;
import uz.pdp.eventorganizerbot.entity.TelegramUser;
import uz.pdp.eventorganizerbot.repo.EventRepo;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestService {

    private final EventRepo eventRepo;
    Faker faker = new Faker();
    Random random = new Random();

    public void createEvents(TelegramUser user) {
        faker.address().city();
        for (int i = 0; i < 10; i++) {
            LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(random.nextInt(10));
            Event event = Event.builder()
                    .venue(faker.address().city())
                    .description(faker.lorem().sentence())
                    .eventDateTime(localDateTime)
                    .title(faker.lorem().word())
                    .maxParticipants(String.valueOf(random.nextInt(20)))
                    .organizer(user)
                    .build();
            eventRepo.save(event);
        }
        for (int i = 0; i < 6; i++) {
            LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(random.nextInt(10));
            Event event = Event.builder()
                    .venue(faker.address().city())
                    .description(faker.lorem().sentence())
                    .eventDateTime(localDateTime)
                    .title(faker.lorem().word())
                    .maxParticipants(String.valueOf(random.nextInt(20)))
                    .organizer(user)
                    .build();
            eventRepo.save(event);
        }
    }

}
