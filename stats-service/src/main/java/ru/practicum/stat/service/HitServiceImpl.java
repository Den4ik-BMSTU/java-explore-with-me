package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.dto.*;
import ru.practicum.stat.exception.RequestParametersException;
import ru.practicum.stat.model.App;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.repositry.AppRepositry;
import ru.practicum.stat.repositry.HitRepositry;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepositry hitRepositry;
    private final AppRepositry appRepositry;
    private final HitMapper mapper;

    @Override
    @Transactional
    public ResponseHitDto createHit(CreateHitDto createHitDto) {
        if (!appRepositry.existsByNameIgnoreCase(createHitDto.getApp())) {
            App app = appRepositry.save(mapper.toApp(createHitDto.getApp()));
            log.info("Add App={}", app);
        }
        App app = appRepositry.findAppByNameIgnoreCase(createHitDto.getApp());
        Hit hit = hitRepositry.save(mapper.toHit(createHitDto, app.getId()));
        log.info("Add Hit={}", hit);
        return mapper.toResponseHitDto(hit, app.getName());
    }

    @Override
    public List<ResponseStatDto> getStats(LocalDateTime start, LocalDateTime end, Boolean unique, List<String> uris) {

        if (!start.isBefore(end)) {
            throw new RequestParametersException(String.format("Error Start=%tc, End=%tc", start, end));
        }
        if (unique) {
            return mapper.toResponseStatDto(hitRepositry.statByUniqueIp(start, end, uris));
        }
        return mapper.toResponseStatDto(hitRepositry.statByIp(start, end, uris));
    }
}
