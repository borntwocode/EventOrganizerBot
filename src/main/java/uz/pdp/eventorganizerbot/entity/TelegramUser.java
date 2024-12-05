package uz.pdp.eventorganizerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.eventorganizerbot.entity.abs.BaseEntity;
import uz.pdp.eventorganizerbot.entity.enums.TgState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "telegram_user")
public class TelegramUser extends BaseEntity {

    @Column(name = "chat_id", unique = true, nullable = false)
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    private String languageCode;

    private String eventName;

    private LocalDateTime eventDateTime;

    private String eventVenue;

    private String eventDescription;

    private String eventMaxParticipants;

    @Enumerated(EnumType.STRING)
    private TgState state;

}
