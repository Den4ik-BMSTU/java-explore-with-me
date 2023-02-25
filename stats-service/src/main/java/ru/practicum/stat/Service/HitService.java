package ru.practicum.stat.Service;

import ru.practicum.stat.Dto.CreateHitDto;
import ru.practicum.stat.Dto.ResponseHitDto;
import ru.practicum.stat.Dto.ResponseStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    ResponseHitDto createHit(CreateHitDto createHitDto);

    List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris);
}
