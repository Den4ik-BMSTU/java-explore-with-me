package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${statistic-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${static-server-app}")
    private String app;

    public void postStats(HttpServletRequest request) {
        NewHit newHit = new NewHit(app, request.getRequestURI(), request.getRemoteAddr());
        post("/hit", newHit);
    }

    public Long getViews(Long eventId) {
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(10).format(format),
                "end", LocalDateTime.now().plusYears(10).format(format),
                "uris", (List.of("/events/" + eventId)),
                "unique", "false"
        );
        ResponseEntity<Object> response = get(url, parameters);

        List<Stats> viewStatsList = response.hasBody() ? (List<Stats>) response.getBody() : List.of();
        return viewStatsList != null && !viewStatsList.isEmpty() ? viewStatsList.get(0).getHits() : 0L;
    }

    public List<Stats> getViewsAll(Set<Long> eventsId) {
        String url = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        Map<String, Object> parameters = Map.of(
                "start",  LocalDateTime.now().minusYears(10).format(format),
                "end", LocalDateTime.now().plusYears(10).format(format),
                "uris", (eventsId.stream().map(id -> "/events/" + id).collect(Collectors.toList())),
                "unique", "false"
        );
        ResponseEntity<Object> response = get(url, parameters);
        return response.hasBody() ? (List<Stats>) response.getBody() : List.of();
    }
}