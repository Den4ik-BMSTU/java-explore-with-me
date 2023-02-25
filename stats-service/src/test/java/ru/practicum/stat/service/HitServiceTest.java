package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stat.dto.CreateHitDto;
import ru.practicum.stat.dto.HitMapper;
import ru.practicum.stat.dto.ResponseHitDto;
import ru.practicum.stat.dto.ResponseStatDto;
import ru.practicum.stat.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class HitServiceTest {
    private final HitService hitService;
    private final HitMapper mapper;

    @Test
    public void createHit() {
        CreateHitDto createHitDto = new CreateHitDto("ewm-main-service", "/events/1", "192.163.0.1");

        ResponseHitDto hit = hitService.createHit(createHitDto);

        MatcherAssert.assertThat("/events/1", equalTo(hit.getUri()));
        MatcherAssert.assertThat("/events/1", equalTo(hit.getUri()));

    }

    @Test
    @Sql("/schemaTest.sql")
    public void getStatNoDistinct() {
        LocalDateTime time = LocalDateTime.now();
        List<String> uris = List.of("/events/1", "/events/2");

        hitService.createHit(new CreateHitDto("ewm-main-service", "/events/1", "192.163.0.1"));
        hitService.createHit(new CreateHitDto("ewm-main-service", "/events/2", "192.163.0.2"));
        hitService.createHit(new CreateHitDto("ewm-main-service", "/events/2", "192.163.0.2"));

        List<ResponseStatDto> hits = hitService.getStats(time.minusDays(10), time.plusDays(10),false, uris);

        MatcherAssert.assertThat(2, equalTo(hits.size()));
        MatcherAssert.assertThat(2L, equalTo(hits.get(0).getHits()));
    }

    @Test
    @Sql("/schemaTest.sql")
    public void getStatDistinct() {
        LocalDateTime time = LocalDateTime.now();
        List<String> uris = List.of("/events/1", "/events/2");

        hitService.createHit(new CreateHitDto("ewm-main-service", "/events/1", "192.163.0.1"));
        hitService.createHit(new CreateHitDto("ewm-main-service", "/events/2", "192.163.0.2"));
        hitService.createHit(new CreateHitDto("ewm-main-service", "/events/2", "192.163.0.2"));

        List<ResponseStatDto> hits = hitService.getStats(time.minusDays(10), time.plusDays(10),true, uris);

        MatcherAssert.assertThat(2, equalTo(hits.size()));
        MatcherAssert.assertThat(1L, equalTo(hits.get(0).getHits()));
    }

    @Test
    public void getStatStartEnd() {
        LocalDateTime time = LocalDateTime.now();
        List<String> uris = List.of("/events/1", "/events/2");
        assertThatThrownBy(() -> {
            List<ResponseStatDto> hits = hitService.getStats(time.plusDays(10), time.minusDays(10),true, uris);
        }).isInstanceOf(Throwable.class);
    }

    @Test
    public void responseHitDtoTest() {
        LocalDateTime created = LocalDateTime.of(2023, 12, 10, 12, 10);
        Hit hit = new Hit(1L, 1L, "/events/1", "192.163.0.1", created);
        ResponseHitDto responseHitDto = new ResponseHitDto(1L, "ewm-main-service", "/events/1", "192.163.0.1", "2023-12-10T12:10:00");

        ResponseHitDto test = mapper.toResponseHitDto(hit, "ewm-main-service");

        assertThat(responseHitDto, equalTo(test));
        assertThat(responseHitDto.hashCode(), equalTo(test.hashCode()));
    }

    @Test
    @Sql("/schemaTest.sql")
    public void hitTest() {
        LocalDateTime created = LocalDateTime.now();
        Hit hit = new Hit(1L, 1L, "/events/1", "192.163.0.1", created);
        CreateHitDto createHitDto = new CreateHitDto("ewm-main-service", "/events/1", "192.163.0.1");

        Hit test = mapper.toHit(createHitDto, 1L);

        assertThat(hit.getAppId(), equalTo(test.getAppId()));
        assertThat(hit.hashCode(), equalTo(test.hashCode()));
    }
}
