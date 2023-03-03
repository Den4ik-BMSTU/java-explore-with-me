package ru.practicum.Comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Event.EventRepository;
import ru.practicum.State.StateEnum;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getAllCommentsByUserId(long userId, long from, long size) {
        return repository.findCommentsByUserId(userId).stream()
                .skip(from)
                .limit(size)
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(long commentId) {
        commentValid(commentId);
        return CommentMapper.toCommentDto(repository.getById(commentId));
    }

    @Override
    public CommentDto saveComment(CommentNewDto commentNewDto, long userId, long eventId) {
        eventPublishValid(eventId);
        return CommentMapper.toCommentDto(repository.save(CommentMapper.toComment(commentNewDto, userId, eventId,
                LocalDateTime.now())));
    }

    @Override
    public CommentDto updateComment(CommentNewDto commentNewDto, long commentId, long userId) {
        commentValid(commentId);
        commentOwnerValid(commentId, userId);
        Comment comment = repository.getById(commentId);
        comment.setComment(commentNewDto.getComment());
        return CommentMapper.toCommentDto(repository.save(comment));
    }

    @Override
    public void removeComment(long userId, long commentId) {
        commentValid(commentId);
        commentOwnerValid(commentId, userId);
        repository.deleteById(commentId);
    }

    private void commentValid(long commentId) {
        if (repository.findCommentById(commentId) == null) {
            log.error("Комментарий c {} не найден", commentId);
            throw new NotFoundException("Комментарий не найден");
        }
    }

    private void commentOwnerValid(long commentId, long userId) {
        if (repository.findCommentById(commentId).getUserId() != userId) {
            log.error("Нельзя изменять чужой комментарий");
            throw new ConflictException("Нельзя изменять чужой комментарий");
        }
    }

    private void eventPublishValid(long eventId) {
        if (!eventRepository.getById(eventId).getState().equals(StateEnum.PUBLISHED.toString())) {
            log.error("Комментарий можно оставлять только к опубликованному событию");
            throw new ConflictException("Комментарий можно оставлять только к опубликованному событию");
        }
    }
}