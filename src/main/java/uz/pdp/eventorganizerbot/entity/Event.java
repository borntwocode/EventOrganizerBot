package uz.pdp.eventorganizerbot.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.eventorganizerbot.entity.abs.BaseEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private TelegramUser organizer;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date", nullable = false)
    private String eventDate;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Column(name = "max_participants")
    private String maxParticipants;

}
