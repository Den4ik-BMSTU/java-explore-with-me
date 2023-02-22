package ru.practicum.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.IncorrectParameterException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEvents(@RequestParam(value = "text") String text,
                                           @RequestParam(value = "categories") List<Long> categories,
                                           @RequestParam(value = "paid") boolean paid,
                                           @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                           @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                           @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                           @RequestParam(value = "sort") String sort,
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
        return eventService.getAllEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable("id") long id) {
        log.info("Событие найдено");
        return eventService.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsOfOwner(@PathVariable("userId") long userId,
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
    public EventFullDto getEventOfOwnerByEventId(@PathVariable("userId") long userId,
                                                @PathVariable("eventId") long eventId) {
        log.info("Событие найдено");
        return eventService.getEventOfOwnerByEventId(userId, eventId);
    }

    @GetMapping("/admin/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(value = "userIds", defaultValue = "0") List<Long> userIds,
                                                @RequestParam(value = "states", required = false) List<String> states,
                                                @RequestParam(value = "categories", defaultValue = "0") List<Long> categories,
                                                @RequestParam(value = "rangeStart") String rangeStart,
                                                @RequestParam(value = "rangeEnd") String rangeEnd,
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
        return eventService.getAllEventsAdmin(userIds, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable("userId") long userId,
                                  @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("Событие добавлено");
        return eventService.saveEvent(userId, eventNewDto);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable("userId") long userId,
                                    @PathVariable("eventId") long eventId,
                                    @Valid @RequestBody EventNewDtoForUpdate eventNewDtoForUpdate) {
        log.info("Событие обновлено");
        return eventService.updateEvent(userId, eventId, eventNewDtoForUpdate);
    }

    @PatchMapping("/admin/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEventAdmin(@PathVariable("eventId") long eventId,
                                         @Valid @RequestBody EventNewDtoForUpdate eventNewDtoForUpdate) {
        log.info("Событие отредактировано");
        return eventService.updateEventAdmin(eventId, eventNewDtoForUpdate);
    }
}