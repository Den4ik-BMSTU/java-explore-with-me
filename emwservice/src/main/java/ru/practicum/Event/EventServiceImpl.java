package ru.practicum.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Category.*;
import ru.practicum.User.UserMapper;
import ru.practicum.User.UserRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.State.StateEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<EventFullDto> getAllEvents(String text, List<Long> categories, boolean paid, String rangeStart,
                                           String rangeEnd, boolean onlyAvailable, String sort, long from, long size) {
        List<Event> events = new ArrayList<>();
        String state = StateEnum.PUBLISHED.toString();
        if ((text == null || text.isEmpty()) || (categories == null || categories.isEmpty()) || rangeStart == null ||
                rangeEnd == null || sort == null){
            events.addAll(repository.findAll().stream()
                    .filter(a -> a.getState().equals(state))
                    .collect(Collectors.toList()));
        } else {
            LocalDateTime start = EventMapper.stringToLocalDateTime(rangeStart);
            LocalDateTime end = EventMapper.stringToLocalDateTime(rangeEnd);
            for (Long category : categories) {
                for (Event event : repository.findEventsPublishedByParameters(start, end, category, paid, state)) {
                    if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                            || event.getDescription().toLowerCase().contains(text.toLowerCase())) {
                        events.add(event);
                    }
                }
            }
            if (sort.equals("EVENT_DATE")) {
                events.sort(Comparator.comparing(Event::getEventDate));
            } else if (sort.equals("VIEWS")) {
                events.sort(Comparator.comparing(Event::getViews).reversed());
            }
        }
        return events.stream()
                .skip(from)
                .limit(size)
                .map(this::toFullEventDtoAbsolutely)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventById(long id) {
        eventValid(id);
        if (repository.getById(id).getState().equals(StateEnum.PUBLISHED.toString())){
            String sqlSelect = "SELECT views FROM events WHERE id = ?";
            Long views = jdbcTemplate.queryForObject(sqlSelect, Long.class, id);
            views++;
            String sqlUpdate = "UPDATE events SET views = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdate, views, id);
            String sql = "SELECT * FROM events WHERE id = ?";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            rowSet.next();
            Event event = repository.findEventById(id);
            event.setViews(rowSet.getLong("views"));
            return toFullEventDtoAbsolutely(event);
        } else {
            log.error("Можно найти только опубликованное событие");
            throw new NotFoundException("Можно найти только опубликованное событие");
        }
    }

    @Override
    public List<EventFullDto> getAllEventsOfOwner(long userId, long from, long size) {
        userValid(userId);
        return repository.findEventsByUserId(userId).stream()
                .skip(from)
                .limit(size)
                .map(this::toFullEventDtoAbsolutely)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventOfOwnerByEventId(long userId, long eventId) {
        userValid(userId);
        eventValid(eventId);
        return toFullEventDtoAbsolutely(repository.findEventByIdAndByUserId(eventId, userId));
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, long from, long size) {
        List<Event> events = new ArrayList<>();
        if ((states == null || states.isEmpty()) || (categories == null || categories.isEmpty())
                || (users == null || users.isEmpty() || rangeStart == null || rangeEnd == null)){
            events.addAll(repository.findAll());
        } else {
            LocalDateTime start = EventMapper.stringToLocalDateTime(rangeStart);
            LocalDateTime end = EventMapper.stringToLocalDateTime(rangeEnd);
            for (String state : states) {
                for (Long category : categories) {
                    for (Long userId : users) {
                        events.addAll(repository.findEventsByParameters(start, end, state, category, userId));
                    }
                }
            }
        }
        return events.stream()
                .skip(from)
                .limit(size)
                .map(this::toFullEventDtoAbsolutely)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto saveEvent(long userId, EventNewDto eventNewDto) {
        userValid(userId);
        eventDateValid(EventMapper.stringToLocalDateTime(eventNewDto.getEventDate()));
        Event event = EventMapper.toEvent(eventNewDto, 0L, LocalDateTime.now(), userId, LocalDateTime.now(),
                StateEnum.PENDING.toString(), 0L);
        repository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event,
                CategoryMapper.toCategoryDto(categoryRepository.getById(eventNewDto.getCategory())),
                UserMapper.toUserDto(userRepository.getById(userId)));
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, EventNewDtoForUpdate eventNewDtoForUpdate) {
        userValid(userId);
        eventValid(eventId);
        eventPublishValid(eventId);
        Event event = repository.getById(eventId);
        if (event.getInitiatorId() != userId) {
            log.error("Пользователь с id {} не может изменять это событие с id {}!", userId, eventId);
            throw new ConflictException("Пользователю запрещено изменять чужое событие!");
        }
        if (eventNewDtoForUpdate.getEventDate() != null){
            eventDateValid(EventMapper.stringToLocalDateTime(eventNewDtoForUpdate.getEventDate()));
        } else {
            eventNewDtoForUpdate.setEventDate(EventMapper.localDateTimeToString(event.getEventDate()));
        }
        saveOldVariablesExceptNull(event, eventNewDtoForUpdate);
        String state = "";
        if (eventNewDtoForUpdate.getStateAction().equals(StateEnum.CANCEL_REVIEW.toString())) {
            state = StateEnum.CANCELED.toString();
        }
        if (eventNewDtoForUpdate.getStateAction().equals(StateEnum.SEND_TO_REVIEW.toString())) {
            state = StateEnum.PENDING.toString();
        }

        Event updatedEvent = EventMapper.toEvent(eventNewDtoForUpdate, event.getConfirmedRequests(), event.getCreatedOn(),
                userId, event.getPublishedOn(), state, event.getViews());

        updatedEvent.setId(eventId);
        repository.save(updatedEvent);
        return toFullEventDtoAbsolutely(updatedEvent);
    }

    @Override
    public EventFullDto updateEventAdmin(long eventId, EventNewDtoForUpdate eventNewDtoForUpdate) {
        eventCancelValid(eventId);
        eventPublishValid(eventId);
        Event event = repository.getById(eventId);
        if (eventNewDtoForUpdate.getEventDate() != null){
            eventDateValidAdmin(EventMapper.stringToLocalDateTime(eventNewDtoForUpdate.getEventDate()));
        } else {
            eventNewDtoForUpdate.setEventDate(EventMapper.localDateTimeToString(event.getEventDate()));
        }
        saveOldVariablesExceptNull(event, eventNewDtoForUpdate);
        String state = "";
        if (eventNewDtoForUpdate.getStateAction().equals(StateEnum.PUBLISH_EVENT.toString())) {
            state = StateEnum.PUBLISHED.toString();
        }
        if (eventNewDtoForUpdate.getStateAction().equals(StateEnum.REJECT_EVENT.toString())) {
            state = StateEnum.REJECTED.toString();
        }
        long userId = event.getInitiatorId();
        Event updatedEvent = EventMapper.toEvent(eventNewDtoForUpdate, event.getConfirmedRequests(), event.getCreatedOn(), userId,
                LocalDateTime.now(), state, event.getViews());
        updatedEvent.setId(eventId);
        repository.save(updatedEvent);
        return toFullEventDtoAbsolutely(updatedEvent);
    }

    private void saveOldVariablesExceptNull(Event event, EventNewDtoForUpdate eventNewDtoForUpdate) {
        if (eventNewDtoForUpdate.getAnnotation() == null) {
            eventNewDtoForUpdate.setAnnotation(event.getAnnotation());
        }
        if (eventNewDtoForUpdate.getCategory() == null) {
            eventNewDtoForUpdate.setCategory(event.getCategory());
        }
        if (eventNewDtoForUpdate.getDescription() == null) {
            eventNewDtoForUpdate.setDescription(event.getDescription());
        }
        if (eventNewDtoForUpdate.getLocation() == null) {
            eventNewDtoForUpdate.setLocation(new Location(event.getLat(), event.getLon()));
        }
        if (eventNewDtoForUpdate.getPaid() == null) {
            eventNewDtoForUpdate.setPaid(event.getPaid());
        }
        if (eventNewDtoForUpdate.getParticipantLimit() == null) {
            eventNewDtoForUpdate.setParticipantLimit(event.getParticipantLimit());
        }
        if (eventNewDtoForUpdate.getRequestModeration() == null) {
            eventNewDtoForUpdate.setRequestModeration(event.getRequestModeration());
        }
        if (eventNewDtoForUpdate.getTitle() == null) {
            eventNewDtoForUpdate.setTitle(event.getTitle());
        }
    }

    private EventFullDto toFullEventDtoAbsolutely(Event event) {
        return EventMapper.toEventFullDto(event,
                CategoryMapper.toCategoryDto(categoryRepository.getById(event.getCategory())),
                UserMapper.toUserDto(userRepository.getById(event.getInitiatorId())));
    }

    private void eventPublishValid(long eventId) {
        if (repository.getById(eventId).getState().equals(StateEnum.PUBLISHED.toString())) {
            log.error("Опубликованное событие c id {} изменить нельзя", eventId);
            throw new ConflictException("Опубликованное событие изменить нельзя");
        }
    }

    private void eventCancelValid(long eventId) {
        if (repository.getById(eventId).getState().equals(StateEnum.REJECTED.toString()) ||
                repository.getById(eventId).getState().equals(StateEnum.CANCELED.toString())) {
            log.error("Отмененное событие c id {} изменить нельзя", eventId);
            throw new ConflictException("Отмененное событие изменить нельзя");
        }
    }

    private void userValid(long userId) {
        if (userRepository.findUserById(userId) == null) {
            log.error("Пользователь c {} не найден или недоступен", userId);
            throw new NotFoundException("Пользователь не найден или недоступен");
        }
    }

    private void eventValid(long eventId) {
        if (repository.findEventById(eventId) == null) {
            log.error("Событие c {} не найдено или недоступно", eventId);
            throw new NotFoundException("Событие не найдено или недоступно");
        }
    }

    private void eventDateValid(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            log.error("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
            throw new ConflictException("Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента");
        }
    }

    private void eventDateValidAdmin(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().minusHours(1))) {
            log.error("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            throw new ConflictException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
    }
}