package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    boolean existsByCategory_Id(Long id);

    List<Event> findEventByInitiatorId(Long userId, PageRequest page);

    boolean existsEventByIdAndInitiatorId(Long eventId, Long userId);

    Event getByIdAndState(Long id, State state);

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id = ?1 AND e.state = ?2 AND e.category.id = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    List<Event> findEventByInitiatorIdAndStateAndCategory_IdAndEventDateBetween(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest page);

    Set<Event> findByIdIn(Set<Long> events);

    @Query("SELECT e FROM Event e " +
            "WHERE  upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.title) like upper(concat('%', ?1, '%')) " +
            "AND e.paid = ?2 AND e.eventDate BETWEEN ?3 AND ?4 " +
            "AND e.category.id = ?5 AND e.state = 'PUBLISHED' " +
            "AND size(e.requests) < e.participantLimit OR size(e.requests) = 0"
    )
    List<Event> findEventByAvailable(String text, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, List<Long> categories, PageRequest page);

    @Query("SELECT e FROM Event e " +
            "WHERE  upper(e.annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(e.title) like upper(concat('%', ?1, '%')) " +
            "AND e.paid = ?2 AND e.eventDate BETWEEN ?3 AND ?4 " +
            "AND e.category.id = ?5 AND e.state = 'PUBLISHED' "
    )
    List<Event> findEvent(String text, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, List<Long> categories, PageRequest page);
}
