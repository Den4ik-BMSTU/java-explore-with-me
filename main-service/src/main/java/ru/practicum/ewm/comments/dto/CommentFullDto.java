package ru.practicum.ewm.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.comments.model.CommentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentFullDto {
    private Long id;
    private String text;
    private CommentStatus status;
    private LocalDateTime created;
    private Long event;
}
