package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Sort;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient client;

    @Override
    @Transactional
    public List<EventShortDto> get(String text, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   Boolean onlyAvailable, Sort sort, Integer from, Integer size, List<Long> categories) {
        final PageRequest page = PageRequest.of(from, size);
        rangeStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        rangeEnd = rangeEnd == null ? rangeStart.plusWeeks(1) : rangeEnd;
        List<Event> events = onlyAvailable ? eventRepository.findEventByAvailable(text, paid, rangeStart, rangeEnd, categories, page) :
                eventRepository.findEvent(text, paid, rangeStart, rangeEnd, categories, page);

        if (sort == null) sort = Sort.ALL;
        switch (sort) {
            case VIEWS: // сортировка по просмотрам
                events.sort(Comparator.comparing(Event::getViews));
                break;
            case EVENT_DATE: // сортировка по дате
                events.sort(Comparator.comparing(Event::getEventDate));
                break;
            default:
                break;
        }

        log.info("Get Events={}", events);
        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getById(Long id) {
        Event event = eventRepository.getByIdAndState(id, State.PUBLISHED);
        event.setViews(client.getViews(event.getId()));
        log.info("Get Event={}", event);
        return eventMapper.toEventFullDto(event, event.getRequests() == null ? 0 : event.getRequests().size());
    }
}
