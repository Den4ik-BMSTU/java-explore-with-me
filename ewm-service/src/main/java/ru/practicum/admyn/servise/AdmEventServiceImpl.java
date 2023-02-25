package ru.practicum.admyn.servise;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admyn.storage.AdmCategoryRepository;
import ru.practicum.admyn.storage.AdmEventRepository;
import ru.practicum.admyn.storage.AdmLocationRepository;
import ru.practicum.comon.dto.EventFullDto;
import ru.practicum.comon.dto.UpdateEventDtoRequest;
import ru.practicum.comon.exception.EntityNoAccessException;
import ru.practicum.comon.exception.EntityNotFoundException;
import ru.practicum.comon.maper.EventMapper;
import ru.practicum.comon.model.Category;
import ru.practicum.comon.model.Event;
import ru.practicum.comon.model.QEvent;
import ru.practicum.comon.util.RequestParams;

import java.util.List;

import static ru.practicum.comon.util.State.*;

@Service
@RequiredArgsConstructor
public class AdmEventServiceImpl implements AdmEventService {

    private final AdmEventRepository eventRepository;
    private final AdmCategoryRepository categoryRepository;
    private final AdmLocationRepository locationRepository;
    private final EventMapper eventMapper;


    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventDtoRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("event not found"));
        switch (request.getStateAction()) {
            case PUBLISH_EVENT:
                if (event.getState().equals(PENDING)) {
                    event.setState(PUBLISHED);
                } else {
                    throw new EntityNoAccessException("event don't pending state");
                }
                break;
            case REJECT_EVENT:
                if (event.getState().equals(PENDING)) {
                    event.setState(CANCELED);
                } else {
                    throw new EntityNoAccessException("event don't pending state");
                }
        }
        updateEventFields(event, request);
        Event updatedEvent = eventRepository.save(event);
        return eventMapper.convertToEventFullDto(updatedEvent);
    }

    @Override
    public List<EventFullDto> getEvents(RequestParams params, Pageable pageable) {
        QEvent qEvent = QEvent.event;
        BooleanExpression predicate = qEvent.isNotNull();

        if (params.getUsers() != null) {
            predicate = predicate.and(qEvent.initiator.id.in(params.getUsers()));
        }
        if (params.getStates() != null) {
            predicate = predicate.and(qEvent.state.in(params.getStates()));
        }
        if (params.getCategories() != null) {
            predicate = predicate.and(qEvent.category.id.in(params.getCategories()));
        }
        if (params.getRangeStart() != null) {
            predicate = predicate.and(qEvent.eventDate.after(params.getRangeStart()));
        }
        if (params.getRangeEnd() != null) {
            predicate = predicate.and(qEvent.eventDate.before(params.getRangeEnd()));
        }
        List<Event> events = eventRepository.findAll(predicate, pageable).toList();
        return eventMapper.convertAllToEventFullDto(events);
    }


    private void updateEventFields(Event event, UpdateEventDtoRequest request) {
        if (request.getCategory() != null) {
            Category category = categoryRepository.findById(request.getCategory()).orElseThrow(
                    () -> new EntityNotFoundException("category not found"));
            event.setCategory(category);
        }
        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }
        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }
        if (request.getLocation() != null) {
            event.setLocation(request.getLocation());
            locationRepository.save(request.getLocation());
        }
        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }
    }

}
