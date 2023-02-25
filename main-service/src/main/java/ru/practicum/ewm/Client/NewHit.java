package ru.practicum.ewm.Client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewHit {
    private String app;
    private String uri;
    private String ip;
}
