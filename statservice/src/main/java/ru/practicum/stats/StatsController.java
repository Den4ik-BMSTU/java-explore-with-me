package ru.practicum.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.ViewStats;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(value = "start") String start,
                                    @RequestParam(value = "end") String end,
                                    @RequestParam(value = "uris") List<String> uris,
                                    @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        log.info("Получили всю статистику");
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Сохранили запрос");
        return statsService.saveHit(endpointHitDto);
    }
}
