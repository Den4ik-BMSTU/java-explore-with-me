package ru.practicum.Comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "comment_line", nullable = false)
    String comment;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "event_id", nullable = false)
    Long eventId;

    @Column(name = "created", nullable = false)
    LocalDateTime created;
}