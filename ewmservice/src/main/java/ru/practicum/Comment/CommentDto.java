package ru.practicum.Comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    String comment;
    Long userId;
    Long eventId;
    LocalDateTime created;
}