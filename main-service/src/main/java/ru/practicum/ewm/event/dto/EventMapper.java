package ru.practicum.ewm.event.dto;

import org.mapstruct.*;
import ru.practicum.ewm.categories.dto.CategoryMapper;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.comments.dto.CommentMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.dto.UserMapper;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CategoryMapper.class, UserMapper.class, CommentMapper.class})
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paid", defaultValue = "false")
    @Mapping(target = "participantLimit", defaultValue = "0")
    @Mapping(target = "requestModeration", defaultValue = "true")
    @Mapping(target = "state", constant = "PENDING")
    @Mapping(target = "category", source = "categories")
    @Mapping(target = "initiator", source = "initiator")
    Event toEvent(NewEventDto newEventDto, User initiator, Category categories);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "category", source = "event.category")
    @Mapping(target = "initiator", source = "event.initiator")
    @Mapping(target = "comments", source = "event.comments")
    @Mapping(target = "confirmedRequests", source = "confirmedRequests")
    EventFullDto toEventFullDto(Event event, int confirmedRequests);

    @Mapping(target = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto toEventShortDto(Event event);

    @Mapping(target = "category", source = "categories")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event updateEvent(UpdateEventUserRequest updateEventUserRequest, @MappingTarget Event event, Category categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event updateEvent(AdminUpdateEventRequest adminUpdateEventRequest, @MappingTarget Event event);
}
