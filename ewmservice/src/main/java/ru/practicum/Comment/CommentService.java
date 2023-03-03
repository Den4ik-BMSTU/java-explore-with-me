package ru.practicum.Comment;

import java.util.List;

public interface CommentService {
    List<CommentDto> getAllCommentsByUserId(long userId, long from, long size);

    CommentDto getCommentById(long commentId);

    CommentDto saveComment(CommentNewDto commentNewDto, long userId, long eventId);

    CommentDto updateComment(CommentNewDto commentNewDto, long commentId, long userId);

    void removeComment(long userId, long commentId);
}