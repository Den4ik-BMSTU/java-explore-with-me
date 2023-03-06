package ru.practicum.Comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentNewDto {
    @NotNull
    @NotBlank(message = "Ошибка: comment пустое или содержит только пробелы")
    String comment;
}