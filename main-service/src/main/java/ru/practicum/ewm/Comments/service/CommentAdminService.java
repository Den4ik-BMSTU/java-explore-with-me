package ru.practicum.ewm.Comments.service;

import ru.practicum.ewm.Comments.dto.CommentShortDto;

public interface CommentAdminService {
    CommentShortDto updateAvailable(boolean available, Long commentId);
}
