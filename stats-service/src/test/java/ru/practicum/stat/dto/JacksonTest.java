package ru.practicum.stat.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class JacksonTest {

    @Autowired
    private JacksonTester<CreateHitDto> jsonCreateHitDto;
    @Autowired
    private JacksonTester<ResponseHitDto> jsonResponseHitDto;

    @Test
    void jsonCreateHitDtoTest() throws Exception {
        CreateHitDto createHitDto = new CreateHitDto("ewm-main-service", "/events/1", "192.163.0.1");

        JsonContent<CreateHitDto> result = jsonCreateHitDto.write(createHitDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo("/events/1");
        assertThat(result).extractingJsonPathStringValue("$.ip").isEqualTo("192.163.0.1");
    }

    @Test
    void jsonResponseHitDtoTest() throws Exception {
        ResponseHitDto responseHitDto = new ResponseHitDto(1L, "ewm-main-service", "/events/1", "192.163.0.1", "2022.01.01 12:00:00");

        JsonContent<ResponseHitDto> result = jsonResponseHitDto.write(responseHitDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo("ewm-main-service");
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo("/events/1");
        assertThat(result).extractingJsonPathStringValue("$.ip").isEqualTo("192.163.0.1");
    }
}