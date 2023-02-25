package ru.practicum.ewm.Request.service;

import ru.practicum.ewm.Request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllByUserId(Long userId);
}
