package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comments.dto.CommentFullDto;
import ru.practicum.ewm.comments.dto.CommentMapper;
import ru.practicum.ewm.comments.dto.NewUpdateCommentDto;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.model.CommentStatus;
import ru.practicum.ewm.comments.repository.CommentRepository;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ExistingValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentPrivateServiceImpl implements CommentPrivateService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentFullDto add(Long userId, Long eventId, NewUpdateCommentDto commentDto) {
        User author = isExistsUserById(userId);
        isExistsRequestOrEventByUserId(eventId, userId);
        isExistsEventById(eventId);
        Comment comment = commentMapper.toComment(commentDto, author, eventId);
        comment = commentRepository.save(comment);
        log.info("Add Comment BD comment={}, userId={}, events={}", comment, userId, eventId);
        return commentMapper.toCommentFullDto(comment);
    }

    @Override
    @Transactional
    public CommentFullDto updateById(Long userId, Long commentId, NewUpdateCommentDto commentDto) {
        isExistsUserById(userId);
        Comment comment = isExistsCommentByUserId(userId, commentId);
        comment = commentMapper.toUpdateComment(commentDto, comment);
        log.info("Update Comment BD comment={}, authorId={}", comment, userId);
        return commentMapper.toCommentFullDto(comment);
    }

    @Override
    @Transactional
    public void remove(Long userId, Long commentId) {
        isExistsUserById(userId);
        isExistsCommentByUserId(userId, commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentFullDto getById(Long userId, Long commentId) {
        isExistsUserById(userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment id=%s not found", commentId)));
        log.info("Get Comment BD comment={}, authorId={}", comment, userId);
        return commentMapper.toCommentFullDto(comment);
    }

    @Override
    public List<CommentFullDto> getAll(Long userId, LocalDateTime rangeStart, LocalDateTime rangeEnd, CommentStatus status, Integer from, Integer size) {
        rangeEnd = rangeEnd == null ? LocalDateTime.now() : rangeEnd;
        rangeStart = rangeStart == null ? rangeEnd.minusWeeks(1) : rangeStart;
        Sort sort = Sort.by(DESC, "created");
        final PageRequest page = PageRequest.of(from, size, sort);
        List<Comment> comments;

        comments = status.equals(CommentStatus.ALL) ? commentRepository.findCommitAndUserId(userId, rangeStart, rangeEnd, page) :
                commentRepository.findCommitAndUserIdAndStatus(userId, status, rangeStart, rangeEnd, page);
        return comments.stream()
                .map(commentMapper::toCommentFullDto)
                .collect(Collectors.toList());
    }

    private void isExistsRequestOrEventByUserId(Long eventId, Long userId) {
        if (!requestRepository.existsByEventAndRequesterAndStatus(eventId, userId, State.PUBLISHED)) {
            if (!eventRepository.existsEventByIdAndInitiatorId(eventId, userId)) {
                throw new ExistingValidationException(String.format("User id=%s not participate or not initiator Event id=%s", userId, eventId));
            }
        }
    }

    private User isExistsUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s not found", id)));
    }

    private void isExistsEventById(Long id) {
        eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s not found", id)));
    }

    private Comment isExistsCommentByUserId(Long userId, Long commentId) {
        if (!commentRepository.existsByIdAndAuthor_IdAndStatus(commentId, userId, CommentStatus.PENDING)) {
            throw new NotFoundException(String.format("Comment id=%s User id=%s not found", commentId, userId));
        }
        return commentRepository.findByIdAndAuthor_Id(commentId, userId);
    }


}
