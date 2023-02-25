package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class EventPrivateController {
    private final EventPrivateService eventPrivateService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(code = HttpStatus.CREATED)
    public EventFullDto add(@Validated(Create.class) @RequestBody NewEventDto newEventDto,
                            @PathVariable Long userId) {
        log.info("Create Event={}, userId={}", newEventDto, userId);
        return eventPrivateService.add(newEventDto, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto update(@RequestBody UpdateEventUserRequest updateEvent,
                               @PathVariable Long userId,
                               @PathVariable Long eventId) {
        log.info("Cancellation eventId={}, userId={}", eventId, userId);
        return eventPrivateService.update(userId, eventId, updateEvent);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId,
                                                @PathVariable Long eventId,
                                                @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        log.info("Update Status Request eventId={}, userId={}, requestIds={}", eventId, userId, statusUpdateRequest.getRequestIds());
        return eventPrivateService.updateStatus(userId, eventId, statusUpdateRequest);
    }

    @GetMapping("{userId}/events")
    public List<EventShortDto> getAllEventByUserId(@PathVariable Long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("getAllEvent userId={}, from={}, size={}", userId, from, size);
        return eventPrivateService.getAllEventByUserId(userId, from, size);
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventFullDto getEventByUserId(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        log.info("getEvent eventId={}, userId={}", eventId, userId);
        return eventPrivateService.getEventByUserId(userId, eventId);
    }

    @GetMapping("{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventByRequests(@PathVariable Long userId,
                                                            @PathVariable Long eventId) {
        log.info("getEventByRequests eventId={}, userId={}", eventId, userId);
        return eventPrivateService.getEventByRequests(userId, eventId);
    }
}
