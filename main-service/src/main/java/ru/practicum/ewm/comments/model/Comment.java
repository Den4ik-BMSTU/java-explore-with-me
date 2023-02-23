package ru.practicum.ewm.comments.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(length = 5000, nullable = false)
    private String text;
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
    @CreationTimestamp
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "event_id")
    private Long event;

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", event=" + event +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        return id != null && id.equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
