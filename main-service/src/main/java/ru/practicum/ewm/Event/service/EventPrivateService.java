package ru.practicum.ewm.Event.service;

import ru.practicum.ewm.Event.dto.*;
import ru.practicum.ewm.Request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.Request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {
    EventFullDto add(NewEventDto newEventDto, Long userId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEvent);

    EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest statusUpdateRequest);

    List<EventShortDto> getAllEventByUserId(Long userId, Integer from, Integer size);

    EventFullDto getEventByUserId(Long userId, Long eventId);

    List<ParticipationRequestDto> getEventByRequests(Long userId, Long eventId);
}
