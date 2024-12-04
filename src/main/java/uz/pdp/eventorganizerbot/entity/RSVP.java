package uz.pdp.eventorganizerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.eventorganizerbot.entity.abs.BaseEntity;
import uz.pdp.eventorganizerbot.entity.enums.RSVPStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "rsvp")
public class RSVP extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "telegram_user_id", nullable = false)
    private TelegramUser user;

    @Column(name = "status")
    private RSVPStatus status;

}
