package ru.practicum.model;

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
@Table(name = "endpoint_hit", schema = "public")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "app", nullable = false)
    String app;

    @Column(name = "uri", nullable = false)
    String uri;

    @Column(name = "ip", nullable = false)
    String ip;

    @Column(name = "time_stamp", nullable = false)
    LocalDateTime timestamp;
}
