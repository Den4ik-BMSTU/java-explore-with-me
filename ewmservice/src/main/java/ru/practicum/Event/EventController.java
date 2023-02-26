package ru.practicum.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Client.Client;
import ru.practicum.Client.EndpointHitDtoMapper;
import ru.practicum.exceptions.IncorrectParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final Client client;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEvents(@RequestParam(value = "text", required = false) String text,
                                           @RequestParam(value = "categories", required = false) List<Long> categories,
                                           @RequestParam(value = "paid", required = false) boolean paid,
                                           @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                           @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                           @RequestParam(value = "onlyAvailable", required = false) boolean onlyAvailable,
                                           @RequestParam(value = "sort", required = false) String sort,
                                           @RequestParam(value = "from", defaultValue = "0") long from,
                                           @RequestParam(value = "size", defaultValue = "10") long size,
                                           HttpServletRequest request) {
        if (from < 0) {
            log.info("Неверный параметр from: {}, from должен быть больше или равен 0 ", from);
            throw new IncorrectParameterException("Неверный параметр from: {}, from должен быть больше или равен 0 " + from);
        }
        if (size <= 0) {
            log.info("Неверный параметр size: {}, size должен быть больше или равен 0 ", size);
            throw new IncorrectParameterException("Неверный параметр size: {}, size должен быть больше или равен 0 " + size);
        }
        log.info("События найдены");
        List<EventFullDto> events = eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
        client.saveStat(EndpointHitDtoMapper.toEndpointHitDto(request.getRemoteAddr(), request.getRequestURI()));
        log.info("Статистика просмотров сохранена");
        return events;
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable long id, HttpServletRequest request) {
        client.saveStat(EndpointHitDtoMapper.toEndpointHitDto(request.getRemoteAddr(), request.getRequestURI()));
        log.info("Событие найдено");
        log.info("Статистика просмотров сохранена");
        return eventService.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsOfOwner(@PathVariable long userId,
                                                  @RequestParam(value = "from", defaultValue = "0") long from,
                                                  @RequestParam(value = "size", defaultValue = "10") long size) {
        if (from < 0) {
            log.info("Неверный параметр from: {}, from должен быть больше или равен 0 ", from);
            throw new IncorrectParameterException("Неверный параметр from: {}, from должен быть больше или равен 0 " + from);
        }
        if (size <= 0) {
            log.info("Неверный параметр size: {}, size должен быть больше или равен 0 ", size);
            throw new IncorrectParameterException("Неверный параметр size: {}, size должен быть больше или равен 0 " + size);
        }
        log.info("События найдены");
        return eventService.getAllEventsOfOwner(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventOfOwnerByEventId(@PathVariable long userId,
                                                 @PathVariable long eventId) {
        log.info("Событие найдено");
        return eventService.getEventOfOwnerByEventId(userId, eventId);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(value = "users", required = false) List<Long> users,
                                                @RequestParam(value = "states", required = false) List<String> states,
                                                @RequestParam(value = "categories", required = false) List<Long> categories,
                                                @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                @RequestParam(value = "from", defaultValue = "0") long from,
                                                @RequestParam(value = "size", defaultValue = "10") long size) {
        if (from < 0) {
            log.info("Неверный параметр from: {}, from должен быть больше или равен 0 ", from);
            throw new IncorrectParameterException("Неверный параметр from: {}, from должен быть больше или равен 0 " + from);
        }
        if (size <= 0) {
            log.info("Неверный параметр size: {}, size должен быть больше или равен 0 ", size);
            throw new IncorrectParameterException("Неверный параметр size: {}, size должен быть больше или равен 0 " + size);
        }
        log.info("События найдены");
        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable long userId,
                                  @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("Событие добавлено");
        return eventService.saveEvent(userId, eventNewDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @Valid @RequestBody EventNewDtoForUpdate eventNewDtoForUpdate) {
        log.info("Событие обновлено");
        return eventService.updateEvent(userId, eventId, eventNewDtoForUpdate);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventAdmin(@PathVariable long eventId,
                                         @Valid @RequestBody EventNewDtoForUpdate eventNewDtoForUpdate) {
        log.info("Событие отредактировано");
        return eventService.updateEventAdmin(eventId, eventNewDtoForUpdate);
    }
}