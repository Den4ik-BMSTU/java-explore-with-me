package ru.practicum.ewm.comments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comments.dto.CommentShortDto;
import ru.practicum.ewm.comments.service.CommentAdminService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comment")
public class CommentAdminController {

    private final CommentAdminService commentService;

    @PatchMapping("/{commentId}")
    public CommentShortDto updateAvailable(@PathVariable Long commentId,
                                            @RequestParam boolean available) {
        log.info("Update available={}, commentId={}", available, commentId);
        return commentService.updateAvailable(available, commentId);
    }
}
