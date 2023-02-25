package ru.practicum.StatClient;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {
    Long id;
    @NotNull
    @NotBlank(message = "Ошибка: app пустое или содержит только пробелы")
    String app;
    @NotNull
    @NotBlank(message = "Ошибка: uri пустое или содержит только пробелы")
    String uri;
    @NotNull
    @NotBlank(message = "Ошибка: ip пустое или содержит только пробелы")
    String ip;
    @NotNull
    @NotBlank(message = "Ошибка: timestamp пустое или содержит только пробелы")
    String timestamp;
}