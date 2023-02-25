package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.model.Sort;
import ru.practicum.ewm.event.service.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventPublicService eventPublicService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> get(@RequestParam(required = false) String text,
                                   @RequestParam(required = false) List<Long> categories,
                                   @RequestParam(required = false) Boolean paid,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                   @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                   @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                   @RequestParam(required = false) Sort sort,
                                   @PositiveOrZero @RequestParam (defaultValue = "0") Integer from,
                                   @Positive @RequestParam(defaultValue = "10") Integer size,
                                   HttpServletRequest request) {
        log.info("Get Event text={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size{}, /n categories={}",
                text, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, categories);
        statsClient.postStats(request);  // Отправить запрос клиенту на добавление в статистику
        return eventPublicService.get(text, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, categories);
    }

    @GetMapping("/{id}")
    public EventFullDto getById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Get Event eventId={}", id);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        statsClient.postStats(request); // Отправить запрос клиенту на добавление в статистику
        return eventPublicService.getById(id);
    }
}
