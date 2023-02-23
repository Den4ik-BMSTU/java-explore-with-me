package ru.practicum.stat.service;

import ru.practicum.stat.dto.CreateHitDto;
import ru.practicum.stat.dto.ResponseHitDto;
import ru.practicum.stat.dto.ResponseStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    ResponseHitDto createHit(CreateHitDto createHitDto);

    List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris);
}
