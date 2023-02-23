package ru.practicum.stat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatDto {
    private String app;
    private String uri;
    private Long hits;
}
