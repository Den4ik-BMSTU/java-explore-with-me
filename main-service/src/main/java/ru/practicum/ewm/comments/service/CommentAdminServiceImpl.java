package ru.practicum.ewm.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.comments.dto.CommentMapper;
import ru.practicum.ewm.comments.dto.CommentShortDto;
import ru.practicum.ewm.comments.model.Comment;
import ru.practicum.ewm.comments.model.CommentStatus;
import ru.practicum.ewm.comments.repository.CommentRepository;
import ru.practicum.ewm.exception.NotFoundException;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public CommentShortDto updateAvailable(boolean available, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment id=%s not found", commentId)));
        CommentStatus status = available ? CommentStatus.APPROVED : CommentStatus.REJECTED;
        comment.setStatus(status);
        return commentMapper.toCommentShortDto(comment);
    }
}
