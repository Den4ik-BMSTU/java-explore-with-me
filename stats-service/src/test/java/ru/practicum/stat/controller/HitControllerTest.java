package ru.practicum.stat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.stat.controller.HitController;
import ru.practicum.stat.dto.CreateHitDto;
import ru.practicum.stat.dto.ResponseHitDto;
import ru.practicum.stat.dto.ResponseStatDto;
import ru.practicum.stat.service.HitService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(HitController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HitControllerTest {

    @MockBean
    private HitService hitService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void hitCreate() throws Exception {
        //given
        CreateHitDto createHitDto = new CreateHitDto("ewm-main-service", "/events/1", "192.163.0.1");
        ResponseHitDto responseHitDto = new ResponseHitDto(1L, "ewm-main-service", "/events/1", "192.163.0.1", "2022.01.01 12:00:00");

        when(hitService.createHit(any())).thenReturn(responseHitDto);

        // when + then
        mockMvc.perform(post("/hit")
                        .content(objectMapper.writeValueAsString(createHitDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ip").value(responseHitDto.getIp()));
    }

    @Test
    void get() throws Exception {
        List<ResponseStatDto> hits = new ArrayList<>();
        //given
        when(hitService.getStats(any(), any(), anyBoolean(), anyList())).thenReturn(hits);

        // when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}", new ArrayList<>())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
