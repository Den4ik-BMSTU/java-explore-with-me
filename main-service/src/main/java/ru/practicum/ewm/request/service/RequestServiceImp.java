package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ExistingValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImp implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        isExistsUserById(userId);
        Event event = isExistsEventById(eventId);
        if (requestRepository.existsByRequesterAndEvent(userId, eventId)) {
            log.error("Add Request userId={}, eventId={} Request уже существует", userId, eventId);
            throw new ExistingValidationException("Request уже существует");
        }

        if (eventRepository.existsEventByIdAndInitiatorId(eventId, userId)) {
            log.error("Add Request userId={}, eventId={} Нельзя участвовать в своем событии", userId, eventId);
            throw new ExistingValidationException("Нельзя участвовать в своем событии");
        }

        if (event.getState().equals(State.PENDING)) {
            log.error("Add Request userId={}, eventId={} Событие неопубликованное", userId, eventId);
            throw new ExistingValidationException("Событие неопубликованное");
        }

        if (event.getParticipantLimit() == (event.getRequests().size())) {
            log.error("Add Request userId={}, eventId={} Количество заявок={} равно лимиту={}",
                    userId, eventId, event.getParticipantLimit(), event.getRequests().size());
            throw new ExistingValidationException("Количество заявок равно лимиту");
        }

        Request request = requestRepository.save(requestMapper.toRequest(userId, eventId));

        if (!event.getRequestModeration()) request.setStatus(State.PUBLISHED);
        log.info("Add BD RequestId={}", request.getId());
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request id=%s not found", requestId)));
        request.setStatus(State.CANCELED);
        log.info("Cancel RequestId={}", requestId);
        return requestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getAllByUserId(Long userId) {
        List<Request> requests = requestRepository.findRequestsByRequester(userId);
        return requests.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private void isExistsUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s not found", id)));
        log.error("User id={} not found", id);
    }

    private Event isExistsEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event id=%s not found", id)));
    }
}
