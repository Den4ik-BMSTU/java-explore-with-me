package ru.practicum.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.Category.Category;
import ru.practicum.User.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    List<Event> findEventsInRangeByStateByCategoryIdByUserId(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                             String state, Long category, Long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 " +
            "and e.initiator_id = ?4 ", nativeQuery = true)
    List<Event> findEventsInRangeByCategoryIdByUserId(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                      Long category, Long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.state = ?3 " +
            "and e.initiator_id = ?4 ", nativeQuery = true)
    List<Event> findEventsInRangeByStateIdByUserId(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                   String state, Long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.state = ?3 and e.category_id = ?4 ", nativeQuery = true)
    List<Event> findEventsInRangeByStateByCategoryId(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                     String state, Long category);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.initiator_id = ?3 ", nativeQuery = true)
    List<Event> findEventsInRangeByUserId(LocalDateTime rangeStart, LocalDateTime rangeEnd, Long userId);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 ", nativeQuery = true)
    List<Event> findEventsInRangeByCategoryId(LocalDateTime rangeStart, LocalDateTime rangeEnd, Long category);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.state = ?3 ", nativeQuery = true)
    List<Event> findEventsInRangeByState(LocalDateTime rangeStart, LocalDateTime rangeEnd, String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 " +
            "and e.paid = ?4 and e.confirmed_requests < e.participant_limit and e.state = ?5 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> findEventsAvailablePaidInRangeByCategoryIdSortByEventDate(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                          Long category, boolean paid, String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 " +
            "and e.paid = ?4 and e.confirmed_requests < e.participant_limit  and e.state = ?5 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> findEventsAvailablePaidInRangeByCategoryIdSortByViews(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                      Long category, boolean paid, String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 " +
            "and e.paid = ?4  and e.state = ?5 order by e.event_date desc ", nativeQuery = true)
    List<Event> findEventsPaidInRangeByCategoryIdSortByEventDate(LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                                 Long category, boolean paid, String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.event_date < ?2 and e.category_id = ?3 " +
            "and e.paid = ?4  and e.state = ?5 order by e.views desc ", nativeQuery = true)
    List<Event> findEventsPaidInRangeByCategoryIdSortByViews(LocalDateTime rangeStart, LocalDateTime rangeEnd, Long category,
                                                             boolean paid, String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.category_id = ?2 and e.paid = ?3 and e.confirmed_requests < e.participant_limit " +
            "and e.state = ?4 order by e.event_date desc ", nativeQuery = true)
    List<Event> findEventsAvailablePaidByCategoryIdSortByEventDate(LocalDateTime nowTime, Long category, boolean paid,
                                                                   String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.category_id = ?2 and e.paid = ?3 and e.confirmed_requests < e.participant_limit " +
            "and e.state = ?4 order by e.views desc ", nativeQuery = true)
    List<Event> findEventsAvailablePaidByCategoryIdSortByViews(LocalDateTime nowTime, Long category, boolean paid,
                                                               String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.category_id = ?2 and e.paid = ?3  and e.state = ?5 " +
            "order by e.event_date desc ", nativeQuery = true)
    List<Event> findEventsPaidByCategoryIdSortByEventDate(LocalDateTime nowTime, Long category, boolean paid, String state);

    @Query(value = "select e.* " +
            "from events as e " +
            "where e.event_date > ?1 and e.category_id = ?2 and e.paid = ?3 and e.state = ?4 " +
            "order by e.views desc ", nativeQuery = true)
    List<Event> findEventsPaidByCategoryIdSortByViews(LocalDateTime nowTime, Long category, boolean paid, String state);
}