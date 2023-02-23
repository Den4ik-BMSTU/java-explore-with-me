package ru.practicum.ewm.event.model;

import lombok.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false, length = 2000)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();
    @Column(length = 7000)
    private String description;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
    })
    private Location location;
    @Column(nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    @Column(nullable = false, length = 120)
    private String title;
    @Transient
    private Long views;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations;
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Request> requests;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        return id != null && id.equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
