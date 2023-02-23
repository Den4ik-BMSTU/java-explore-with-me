package ru.practicum.stat.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits", schema = "public")
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "app_id")
    private Long appId;
    @Column(nullable = false, length = 30)
    private String uri;
    @Column(nullable = false, length = 16)
    private String ip;
    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hit)) return false;
        return id != null && id.equals(((Hit) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
