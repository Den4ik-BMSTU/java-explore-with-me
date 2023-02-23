package ru.practicum.Requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Event.EventRepository;
import ru.practicum.State.StateEnum;
import ru.practicum.User.UserRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RequestDto> getAllRequestsOfUser(long userId) {
        userValid(userId);
        return RequestMapper.toListRequestDto(repository.findRequestsByUserId(userId));
    }

    @Override
    public List<RequestDto> getAllRequestsByOwnerIdAndByEventId(long userId, long eventId) {
        userValid(userId);
        eventValid(eventId);
        return RequestMapper.toListRequestDto(repository.findAllByOwnerIdAndByEventId(userId, eventId));
    }

    @Transactional
    @Override
    public RequestDto saveRequest(long userId, long eventId) {
        userValid(userId);
        eventValid(eventId);
        if (repository.findRequestsByUserIdAndByEventId(userId, eventId).size() != 0) {
            log.error("Нельзя добавить повторный запрос");
            throw new ConflictException("Нельзя добавить повторный запрос");
        }
        if (eventRepository.getById(eventId).getInitiatorId() == userId) {
            log.error("Инициатор события не может добавить запрос на участие в своём событии");
            throw new ConflictException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!eventRepository.getById(eventId).getState().equals(StateEnum.PUBLISHED.toString())) {
            log.error("Нельзя участвовать в неопубликованном событии");
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }
        if (Objects.equals(eventRepository.getById(eventId).getConfirmedRequests(), eventRepository.getById(eventId).getParticipantLimit())) {
            log.error("Достигнут лимит запросов на участие");
            throw new ConflictException("Достигнут лимит запросов на участие");
        }
        Request request = RequestMapper.toRequest(userId, eventId);
        if (!eventRepository.getById(eventId).getRequestModeration()) {
            request.setStatus(StateEnum.CONFIRMED.toString());
            String sqlSelect = "SELECT confirmed_requests FROM events WHERE id = ?";
            Long confirmedRequests = jdbcTemplate.queryForObject(sqlSelect, Long.class, eventId);
            confirmedRequests++;
            String sqlUpdate = "UPDATE events SET confirmed_requests = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdate, confirmedRequests, eventId);
        } else {
            request.setStatus(StateEnum.PENDING.toString());
        }
        return RequestMapper.toRequestDto(repository.save(request));
    }

    @Transactional
    @Override
    public RequestDto cancelRequest(long userId, long requestId) {
        userValid(userId);
        requestValid(requestId);
        Request request = repository.getById(requestId);
        if (request.getRequester() != userId) {
            log.error("Пользователь с id {} не может отменить этот запрос с id {}!", userId, requestId);
            throw new ConflictException("Пользователю запрещено отменять чужой запрос!");
        }
        if (request.getStatus().equals(StateEnum.CONFIRMED.toString())) {
            String sqlSelect = "SELECT confirmed_requests FROM events WHERE id = ?";
            Long confirmedRequests = jdbcTemplate.queryForObject(sqlSelect, Long.class, request.getEvent());
            confirmedRequests--;
            String sqlUpdate = "UPDATE events SET confirmed_requests = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdate, confirmedRequests, request.getEvent());
        }
        request.setStatus(StateEnum.CANCELED.toString());
        return RequestMapper.toRequestDto(repository.save(request));
    }

    @Transactional
    @Override
    public RequestListStatusUpdateResult updateRequestsStatus(long userId, long eventId, RequestListStatusUpdate requestListStatusUpdate) {
        userValid(userId);
        eventValid(eventId);
        if (Objects.equals(eventRepository.getById(eventId).getConfirmedRequests(), eventRepository.getById(eventId).getParticipantLimit())) {
            log.error("Достигнут лимит запросов на участие");
            throw new ConflictException("Достигнут лимит запросов на участие");
        }
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        List<Long> requestIds = new ArrayList<>();
        long numberPlacesLeft = eventRepository.getById(eventId).getParticipantLimit() -
                eventRepository.getById(eventId).getConfirmedRequests();
        if (requestListStatusUpdate.getRequestIds().size() > numberPlacesLeft && requestListStatusUpdate.getStatus().equals("CONFIRMED")) {
            requestIds = requestListStatusUpdate.getRequestIds().stream()
                    .map(repository::getById)
                    .sorted(Comparator.comparing(Request::getCreated))
                    .map(Request::getId)
                    .limit(numberPlacesLeft)
                    .collect(Collectors.toList());
            for (Long requestId : requestListStatusUpdate.getRequestIds()) {
                if (!requestIds.contains(requestId)) {
                    Request request = repository.getById(requestId);
                    request.setStatus("REJECTED");
                    repository.save(request);
                    rejectedRequests.add(RequestMapper.toRequestDto(request));
                }
            }
        } else if (requestListStatusUpdate.getRequestIds().size() <= numberPlacesLeft && requestListStatusUpdate.getStatus().equals("CONFIRMED")) {
            requestIds = requestListStatusUpdate.getRequestIds();
        }
        for (Long requestId : requestIds) {
            Request request = repository.getById(requestId);
            request.setStatus(requestListStatusUpdate.getStatus());
            String sqlSelect = "SELECT confirmed_requests FROM events WHERE id = ?";
            Long numberConfirmedRequests = jdbcTemplate.queryForObject(sqlSelect, Long.class, eventId);
            numberConfirmedRequests++;
            String sqlUpdate = "UPDATE events SET confirmed_requests = ? WHERE id = ?";
            jdbcTemplate.update(sqlUpdate, numberConfirmedRequests, eventId);
            String sql = "SELECT * FROM events WHERE id = ?";
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, eventId);
            rowSet.next();
            repository.save(request);
            confirmedRequests.add(RequestMapper.toRequestDto(request));
        }
        if (requestListStatusUpdate.getStatus().equals("REJECTED")) {
            requestIds = requestListStatusUpdate.getRequestIds();
            for (Long requestId : requestIds) {
                Request request = repository.getById(requestId);
                request.setStatus(requestListStatusUpdate.getStatus());
                repository.save(request);
                rejectedRequests.add(RequestMapper.toRequestDto(request));
            }
        }
        return new RequestListStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void requestValid(long requestId) {
        if (repository.findRequestById(requestId) == null) {
            log.error("Запрос c {} не найден или недоступен", requestId);
            throw new NotFoundException("Запрос не найден или недоступен");
        }
    }

    private void userValid(long userId) {
        if (userRepository.findUserById(userId) == null) {
            log.error("Пользователь c {} не найден или недоступен", userId);
            throw new NotFoundException("Пользователь не найден или недоступен");
        }
    }

    private void eventValid(long eventId) {
        if (eventRepository.findEventById(eventId) == null) {
            log.error("Событие c {} не найдено или недоступно", eventId);
            throw new NotFoundException("Событие не найдено или недоступно");
        }
    }
}