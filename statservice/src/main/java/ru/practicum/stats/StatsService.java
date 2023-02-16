package ru.practicum.stats;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.ViewStats;

import java.util.List;

public interface StatsService {
    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);

    EndpointHitDto saveHit(EndpointHitDto endpointHitDto);
}