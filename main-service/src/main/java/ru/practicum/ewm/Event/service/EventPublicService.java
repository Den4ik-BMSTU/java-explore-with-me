package ru.practicum.ewm.Event.service;

import ru.practicum.ewm.Event.dto.EventFullDto;
import ru.practicum.ewm.Event.dto.EventShortDto;
import ru.practicum.ewm.Event.model.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {

    List<EventShortDto> get(String text, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                            Boolean onlyAvailable, Sort sort, Integer from, Integer size, List<Long> categories);

    EventFullDto getById(Long id);
}
