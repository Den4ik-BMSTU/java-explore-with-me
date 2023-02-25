package ru.practicum.ewm.Comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Comments.model.CommentStatus;

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
