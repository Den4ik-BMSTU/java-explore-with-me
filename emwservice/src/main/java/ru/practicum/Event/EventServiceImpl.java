package ru.practicum.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Category.*;
import ru.practicum.User.UserMapper;
import ru.practicum.User.UserRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
        if (rangeStart != null && rangeEnd != null){
            LocalDateTime start = EventMapper.stringToLocalDateTime(rangeStart);
            LocalDateTime end = EventMapper.stringToLocalDateTime(rangeEnd);
            if (onlyAvailable == true){
                if (sort.equals("EVENT_DATE")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsAvailablePaidInRangeByCategoryIdSortByEventDate(start, end, category,
                                paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                } else if (sort.equals("VIEWS")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsAvailablePaidInRangeByCategoryIdSortByViews(start, end, category,
                                paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                }
            } else {
                if (sort.equals("EVENT_DATE")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsPaidInRangeByCategoryIdSortByEventDate(start, end, category,
                                paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                } else if (sort.equals("VIEWS")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsPaidInRangeByCategoryIdSortByViews(start, end, category,
                                paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                }
            }
        } else if (rangeStart == null && rangeEnd == null){
            if (onlyAvailable == true){
                if (sort.equals("EVENT_DATE")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsAvailablePaidByCategoryIdSortByEventDate(LocalDateTime.now(),
                                category, paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                } else if (sort.equals("VIEWS")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsAvailablePaidByCategoryIdSortByViews(LocalDateTime.now(),
                                category, paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                }
            } else {
                if (sort.equals("EVENT_DATE")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsPaidByCategoryIdSortByEventDate(LocalDateTime.now(),
                                category, paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
                    }
                } else if (sort.equals("VIEWS")){
                    for (Long category : categories) {
                        for (Event event : repository.findEventsPaidByCategoryIdSortByViews(LocalDateTime.now(),
                                category, paid, state)) {
                            if (event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                                    || event.getDescription().toLowerCase().contains(text.toLowerCase())){
                                events.add(event);
                            }
                        }
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
    public EventFullDto getEventById(long id) {
        eventValid(id);
        if (repository.getById(id).getState().equals(StateEnum.PUBLISHED.toString())){
            String sqlSelect = "SELECT views FROM events WHERE id = ?";
            Long views = jdbcTemplate.queryForObject(sqlSelect, Long.class, id);
            views++;
            String sqlUpdate = "UPDATE events SET views = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdate, views, id);
            return toFullEventDtoAbsolutely(repository.findEventById(id));
        } else {
            log.error("Можно найти только опубликованное событие");
            throw new NotFoundException("Можно найти только опубликованное событие");
        }
    }

    @Override
    public List<EventFullDto> getAllEventsByUserId(long userId, long from, long size) {
        userValid(userId);
        return repository.findEventsByUserId(userId).stream()
                .skip(from)
                .limit(size)
                .map(this::toFullEventDtoAbsolutely)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByIdAndByUserId(long userId, long eventId) {
        userValid(userId);
        eventValid(eventId);
        return toFullEventDtoAbsolutely(repository.findEventByIdAndByUserId(eventId, userId));
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> userIds, List<String> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, long from, long size) {
        LocalDateTime start = EventMapper.stringToLocalDateTime(rangeStart);
        LocalDateTime end = EventMapper.stringToLocalDateTime(rangeEnd);
        List<Event> events = new ArrayList<>();
        if (states.size() == 0) {
            for (Long category : categories) {
                for (Long userId : userIds) {
                    events.addAll(repository.findEventsInRangeByCategoryIdByUserId(start, end, category, userId));
                }
            }
        } else if (userIds.size() == 0) {
            for (String state : states) {
                for (Long category : categories) {
                    events.addAll(repository.findEventsInRangeByStateByCategoryId(start, end, state, category));
                }
            }
        } else if (categories.size() == 0) {
            for (String state : states) {
                for (Long userId : userIds) {
                    events.addAll(repository.findEventsInRangeByStateIdByUserId(start, end, state, userId));
                }
            }
        }
        if (categories.size() == 0 && states.size() == 0) {
            for (Long userId : userIds) {
                events.addAll(repository.findEventsInRangeByUserId(start, end, userId));
            }
        } else if (categories.size() == 0 && userIds.size() == 0) {
            for (String state : states) {
                events.addAll(repository.findEventsInRangeByState(start, end, state));
            }
        } else if (states.size() == 0 && userIds.size() == 0) {
            for (Long category : categories) {
                events.addAll(repository.findEventsInRangeByCategoryId(start, end, category));
            }
        }
        for (String state : states) {
            for (Long category : categories) {
                for (Long userId : userIds) {
                    events.addAll(repository.findEventsInRangeByStateByCategoryIdByUserId(start, end, state, category, userId));
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
        EventFullDto eventFullDto = EventMapper.toFullEventDto(event,
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

        Event updatedEvent = EventMapper.toEvent(eventNewDtoForUpdate, event.getConfirmedRequests(), event.getCreatedOn(),
                userId, event.getPublishedOn(), StateEnum.PENDING.toString(), event.getViews());

        updatedEvent.setId(eventId);
        repository.save(updatedEvent);
        return toFullEventDtoAbsolutely(updatedEvent);
    }

    @Override
    public EventFullDto updateEventAdmin(long eventId, EventNewDtoForUpdate eventNewDtoForUpdate) {
        eventPublishValid(eventId);
        Event event = repository.getById(eventId);
        if (eventNewDtoForUpdate.getEventDate() != null){
            eventDateValidAdmin(EventMapper.stringToLocalDateTime(eventNewDtoForUpdate.getEventDate()));
        } else {
            eventNewDtoForUpdate.setEventDate(EventMapper.localDateTimeToString(event.getEventDate()));
        }
        saveOldVariablesExceptNull(event, eventNewDtoForUpdate);
        String state = "";
        if (eventNewDtoForUpdate.getStateAction().equals(StateActionEnum.PUBLISH_EVENT.toString())) {
            state = StateEnum.PUBLISHED.toString();
        }
        if (eventNewDtoForUpdate.getStateAction().equals(StateActionEnum.CANCEL_EVENT.toString())) {
            state = StateEnum.CANCELED.toString();
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
        return EventMapper.toFullEventDto(event,
                CategoryMapper.toCategoryDto(categoryRepository.getById(event.getCategory())),
                UserMapper.toUserDto(userRepository.getById(event.getInitiatorId())));
    }

    private void eventPublishValid(long eventId) {
        if (repository.getById(eventId).getState().equals(StateEnum.PUBLISHED.toString())) {
            log.error("Опубликованное событие c id {} изменить нельзя", eventId);
            throw new ConflictException("Опубликованное событие изменить нельзя");
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