package ru.practicum.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventById(Long id);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.initiator_id = ?1 ", nativeQuery = true)
    List<Event> findEventsByUserId(long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.id = ?1 and e.initiator_id = ?2", nativeQuery = true)
    Event findEventByIdAndByUserId(long eventId, long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.state = ?3 and e.category_id = ?4 " +
            "and e.initiator_id = ?5 ", nativeQuery = true)
    List<Event> findEventsByParameters(LocalDateTime rangeStart, LocalDateTime rangeEnd, String state, Long category, Long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 " +
            "and e.paid = ?4 and e.state = ?5 ", nativeQuery = true)
    List<Event> findEventsPublishedByParameters(LocalDateTime rangeStart, LocalDateTime rangeEnd, Long category,
                                                boolean paid, String state);

}