package ru.practicum.ewm.Comments.dto;

import org.mapstruct.*;
import ru.practicum.ewm.Comments.model.Comment;
import ru.practicum.ewm.User.model.User;

@Mapper (componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", source = "author")
    @Mapping(target = "event", source = "eventId")
    @Mapping(target = "status", constant = "PENDING")
    Comment toComment(NewUpdateCommentDto newUpdateCommentDto, User author, Long eventId);

    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentShortDto toCommentShortDto(Comment comment);

    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentFullDto toCommentFullDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment toUpdateComment(NewUpdateCommentDto newUpdateCommentDto, @MappingTarget Comment comment);
}
