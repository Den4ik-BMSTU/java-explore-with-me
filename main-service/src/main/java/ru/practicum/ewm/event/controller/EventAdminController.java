package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.service.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventAdminService eventAdminService;

    @PatchMapping("/{eventId}")
    public EventFullDto patch(@PathVariable Long eventId,
                              @RequestBody @Valid AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Admin update EventId={}, updateEvent={}", eventId, adminUpdateEventRequest);
        return eventAdminService.patch(eventId, adminUpdateEventRequest);
    }

    @GetMapping
    public List<EventFullDto> get(@RequestParam(required = false) List<Long> users,
                                  @RequestParam(required = false) List<State> states,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Admin get users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventAdminService.get(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
