package ru.practicum.Compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.Event.EventFullDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Long id;

    List<EventFullDto> events;

    Boolean pinned;

    @NotNull
    @NotBlank(message = "Ошибка: title пустое или содержит только пробелы")
    String title;
}