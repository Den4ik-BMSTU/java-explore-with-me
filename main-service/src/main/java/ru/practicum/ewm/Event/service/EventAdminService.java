package ru.practicum.ewm.Event.service;

import ru.practicum.ewm.Event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.Event.dto.EventFullDto;
import ru.practicum.ewm.Event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    EventFullDto patch(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    List<EventFullDto> get(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);
}
