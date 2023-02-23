package ru.practicum.Requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {
    Long id;

    LocalDateTime created;

    Long event;

    Long requester;

    @NotNull
    @NotBlank(message = "Ошибка: status пустое или содержит только пробелы")
    String status;
}
