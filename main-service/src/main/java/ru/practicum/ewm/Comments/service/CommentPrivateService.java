package ru.practicum.ewm.Comments.service;

import ru.practicum.ewm.Comments.dto.CommentFullDto;
import ru.practicum.ewm.Comments.dto.NewUpdateCommentDto;
import ru.practicum.ewm.Comments.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentPrivateService {
    CommentFullDto add(Long userId, Long eventId, NewUpdateCommentDto commentDto);

    CommentFullDto updateById(Long userId, Long commentId, NewUpdateCommentDto commentDto);

    void remove(Long userId, Long commentId);

    CommentFullDto getById(Long userId, Long commentId);

    List<CommentFullDto> getAll(Long userId, LocalDateTime rangeStart, LocalDateTime rangeEnd, CommentStatus status, Integer from, Integer size);
}
