package ru.practicum.Event;

import java.util.List;

public interface EventService {
    List<EventFullDto> getAllEvents(String text, List<Long> categories, boolean paid, String rangeStart, String rangeEnd,
                                    boolean onlyAvailable, String sort, long from, long size);

    EventFullDto getEventById(long id);

    List<EventFullDto> getAllEventsOfOwner(long userId, long from, long size);

    EventFullDto getEventOfOwnerByEventId(long userId, long eventId);

    List<EventFullDto> getAllEventsAdmin(List<Long> users, List<String> states, List<Long> categories, String rangeStart,
                                         String rangeEnd, long from, long size);

    EventFullDto saveEvent(long userId, EventNewDto eventNewDto);

    EventFullDto updateEvent(long userId, long eventId, EventNewDtoForUpdate eventNewDtoForUpdate);

    EventFullDto updateEventAdmin(long eventId, EventNewDtoForUpdate eventNewDtoForUpdate);
}