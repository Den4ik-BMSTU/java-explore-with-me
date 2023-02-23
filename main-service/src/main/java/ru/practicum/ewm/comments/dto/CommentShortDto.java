package ru.practicum.ewm.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    private Long id;
    private String text;
    private UserShortDto author;
    private String created;
}
