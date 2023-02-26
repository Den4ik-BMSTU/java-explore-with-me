package ru.practicum.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Event findEventById(Long id);

    @Query(value = "SELECT E.* " +
            "FROM EVENTS AS E " +
            "WHERE E.INITIATOR_ID = ?1 ", nativeQuery = true)
    List<Event> findEventsByUserId(long userId);

    @Query(value = "SELECT E.* " +
            "FROM EVENTS AS E " +
            "WHERE E.ID = ?1 AND e.INITIATOR_ID = ?2", nativeQuery = true)
    Event findEventByIdAndByUserId(long eventId, long userId);

    @Query(value = "SELECT E.* " +
            "FROM EVENTS AS E " +
            "WHERE E.EVENT_DATE > ?1 AND E.EVENT_DATE < ?2 AND E.STATE = ?3 AND E.CATEGORY_ID = ?4 " +
            "AND E.INITIATOR_ID = ?5 ", nativeQuery = true)
    List<Event> findEventsByParameters(LocalDateTime rangeStart, LocalDateTime rangeEnd, String state, Long category,
                                       Long userId);

    @Query(value = "SELECT E.* " +
            "FROM EVENTS AS E " +
            "WHERE E.EVENT_DATE > ?1 AND E.EVENT_DATE < ?2 AND E.CATEGORY_ID = ?3 " +
            "AND E.PAID = ?4 AND E.STATE = ?5 ", nativeQuery = true)
    List<Event> findEventsPublishedByParameters(LocalDateTime rangeStart, LocalDateTime rangeEnd, Long category,
                                                boolean paid, String state);

}