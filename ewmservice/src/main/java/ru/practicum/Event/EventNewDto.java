package ru.practicum.Event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventNewDto {
    @NotNull
    @NotBlank(message = "Ошибка: annotation пустое или содержит только пробелы")
    String annotation;

    Long category;

    @NotNull
    @NotBlank(message = "Ошибка: description пустое или содержит только пробелы")
    String description;

    String eventDate;

    Location location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;

    @NotNull
    @NotBlank(message = "Ошибка: title пустое или содержит только пробелы")
    String title;
}
