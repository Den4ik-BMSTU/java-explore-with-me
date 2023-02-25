package ru.practicum.admyn.servise;

import org.springframework.data.domain.Pageable;
import ru.practicum.comon.dto.EventFullDto;
import ru.practicum.comon.dto.UpdateEventDtoRequest;
import ru.practicum.comon.util.RequestParams;

import java.util.List;

public interface AdmEventService {
    EventFullDto updateEvent(Long eventId, UpdateEventDtoRequest request);

    List<EventFullDto> getEvents(RequestParams params, Pageable pageable);
}
