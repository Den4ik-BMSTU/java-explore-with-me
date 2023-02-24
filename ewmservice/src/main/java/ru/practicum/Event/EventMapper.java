package ru.practicum.Event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.Category.CategoryDto;
import ru.practicum.User.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {

    public static EventFullDto toEventFullDto(Event event, CategoryDto categoryDto, UserDto userDto) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(categoryDto);
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(localDateTimeToString(event.getCreatedOn()));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(localDateTimeToString(event.getEventDate()));
        eventFullDto.setInitiator(userDto);
        eventFullDto.setLocation(new Location(event.getLat(), event.getLon()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(localDateTimeToString(event.getPublishedOn()));
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());
        return eventFullDto;
    }

    public static Event toEvent(EventNewDto eventNewDto, Long confirmedRequests, LocalDateTime createdOn, long userId,
                                LocalDateTime publishedOn, String state, Long views) {
        Event event = new Event();
        event.setAnnotation(eventNewDto.getAnnotation());
        event.setCategory(eventNewDto.getCategory());
        event.setConfirmedRequests(confirmedRequests);
        event.setCreatedOn(createdOn);
        event.setDescription(eventNewDto.getDescription());
        event.setEventDate(stringToLocalDateTime(eventNewDto.getEventDate()));
        event.setInitiatorId(userId);
        event.setLat(eventNewDto.getLocation().getLat());
        event.setLon(eventNewDto.getLocation().getLon());
        event.setPaid(eventNewDto.getPaid());
        event.setParticipantLimit(eventNewDto.getParticipantLimit());
        event.setPublishedOn(publishedOn);
        event.setRequestModeration(eventNewDto.getRequestModeration());
        event.setState(state);
        event.setTitle(eventNewDto.getTitle());
        event.setViews(views);
        return event;
    }

    public static Event toEvent(EventNewDtoForUpdate eventNewDtoForUpdate, Long confirmedRequests, LocalDateTime createdOn,
                                long userId, LocalDateTime publishedOn, String state, Long views) {
        Event event = new Event();
        event.setAnnotation(eventNewDtoForUpdate.getAnnotation());
        event.setCategory(eventNewDtoForUpdate.getCategory());
        event.setConfirmedRequests(confirmedRequests);
        event.setCreatedOn(createdOn);
        event.setDescription(eventNewDtoForUpdate.getDescription());
        event.setEventDate(stringToLocalDateTime(eventNewDtoForUpdate.getEventDate()));
        event.setInitiatorId(userId);
        event.setLat(eventNewDtoForUpdate.getLocation().getLat());
        event.setLon(eventNewDtoForUpdate.getLocation().getLon());
        event.setPaid(eventNewDtoForUpdate.getPaid());
        event.setParticipantLimit(eventNewDtoForUpdate.getParticipantLimit());
        event.setPublishedOn(publishedOn);
        event.setRequestModeration(eventNewDtoForUpdate.getRequestModeration());
        event.setState(state);
        event.setTitle(eventNewDtoForUpdate.getTitle());
        event.setViews(views);
        event.setStateAction(eventNewDtoForUpdate.getStateAction());
        return event;
    }

    public static LocalDateTime stringToLocalDateTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, formatter);
        return localDateTime;
    }

    public static String localDateTimeToString(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String localDateTime = timestamp.format(formatter);
        return localDateTime;
    }
}