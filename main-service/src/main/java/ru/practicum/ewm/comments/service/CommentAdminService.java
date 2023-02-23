package ru.practicum.ewm.comments.service;

import ru.practicum.ewm.comments.dto.CommentShortDto;

public interface CommentAdminService {
    CommentShortDto updateAvailable(boolean available, Long commentId);
}
