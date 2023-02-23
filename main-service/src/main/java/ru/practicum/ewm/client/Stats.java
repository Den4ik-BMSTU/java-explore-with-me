package ru.practicum.ewm.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    private String app;
    private String uri;
    private Long hits;
}
