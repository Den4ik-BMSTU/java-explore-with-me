package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    EventFullDto patch(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    List<EventFullDto> get(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
