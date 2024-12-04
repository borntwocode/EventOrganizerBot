package uz.pdp.eventorganizerbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class EventOrganizerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventOrganizerBotApplication.class, args);
    }

}
