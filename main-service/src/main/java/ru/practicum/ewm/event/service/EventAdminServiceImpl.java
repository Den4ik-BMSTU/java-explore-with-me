package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.Stats;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ExistingValidationException;
import ru.practicum.ewm.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventAdminServiceImpl implements EventAdminService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient client;

    @Override
    @Transactional
    public EventFullDto patch(Long eventId, AdminUpdateEventRequest updateEvent) {
        Event event = getEventById(eventId);

        if (event.getEventDate().isBefore(LocalDateTime.now().minusHours(1))) {
            log.error("Невозможно изменить событие");
            throw new ExistingValidationException("Невозможно опубликовать событие");
        }

        switch (updateEvent.getStateAction()) {
            case PUBLISH_EVENT:
                if (!event.getState().equals(State.PENDING)) {
                    log.error("Невозможно опубликовать событие");
                    throw new ExistingValidationException("Невозможно опубликовать событие");
                }
                event.setState(State.PUBLISHED);
                break;
            case REJECT_EVENT:
                if (event.getState().equals(State.PUBLISHED)) {
                    log.error("Невозможно отменить событие");
                    throw new ExistingValidationException("Невозможно отменить событие");
                }
                event.setState(State.CANCELED);
                break;
        }
        event.setViews(client.getViews(event.getId()));

        event = eventMapper.updateEvent(updateEvent, event);
        log.info("Put EventId={}", event.getId());
        return eventMapper.toEventFullDto(event, event.getRequests() == null ? 0 : event.getRequests().size());
    }

    @Override
    public List<EventFullDto> get(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from, size);
        List<Event> events = eventRepository.findEventByInitiatorIdAndStateAndCategory_IdAndEventDateBetween(users, states, categories, rangeStart, rangeEnd, page);
        addViews(events);
        return events.stream()
                .map(event -> eventMapper.toEventFullDto(event, event.getRequests() == null ? 0 : event.getRequests().size()))
                .collect(Collectors.toList());
    }

    private Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s not found", id)));

    }

    private void addViews(List<Event> events) {
        Map<Long, Event> eventMap = events.stream().collect(Collectors.toMap(Event::getId, event -> event));
        List<Stats> views = client.getViewsAll(eventMap.keySet());
        views.forEach(h -> eventMap.get(Long.parseLong(h.getUri().split("/")[1])).setViews(h.getHits()));
    }
}
