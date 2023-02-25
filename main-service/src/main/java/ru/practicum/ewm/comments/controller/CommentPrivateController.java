package ru.practicum.ewm.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.CommentFullDto;
import ru.practicum.ewm.comments.dto.NewUpdateCommentDto;
import ru.practicum.ewm.comments.model.CommentStatus;
import ru.practicum.ewm.comments.service.CommentPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/users")
public class CommentPrivateController {

    private final CommentPrivateService commentService;

    @PostMapping("/{userId}/{eventId}/comments")
    @ResponseStatus(code = HttpStatus.CREATED)
    public CommentFullDto add(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Valid NewUpdateCommentDto commentDto) {
        log.info("Create Comment={}, userId={}, eventId={}", commentDto, userId, eventId);
        return commentService.add(userId, eventId, commentDto);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentFullDto updateById(@PathVariable Long userId,
                                      @PathVariable Long commentId,
                                      @RequestBody @Valid NewUpdateCommentDto commentDto) {
        log.info("Update CommentDto={}, userId={}, commentId={}", commentDto, userId, commentId);
        return commentService.updateById(userId, commentId, commentDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        log.info("Remove commentId={}, userId={}", commentId, userId);
        commentService.remove(userId, commentId);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentFullDto getById(@PathVariable Long userId,
                                  @PathVariable Long commentId) {
        log.info("Get commentId={}, userId={}", commentId, userId);
        return commentService.getById(userId, commentId);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentFullDto> getAll(@PathVariable Long userId,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                       @RequestParam (defaultValue = "ALL") CommentStatus status,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get Comment userId={}, rangeStart={}, rangeEnd={}, available={}, from={}, size={}",
                userId, rangeStart, rangeEnd, status, from, size);
        return commentService.getAll(userId, rangeStart, rangeEnd, status, from, size);
    }
}
