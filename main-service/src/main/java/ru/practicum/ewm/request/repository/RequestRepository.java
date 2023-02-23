package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.event.model.State;
import ru.practicum.ewm.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean existsByEventAndRequesterAndStatus(Long event, Long requester, State state);

    boolean existsByRequesterAndEvent(Long requester, Long event);

    List<Request> findByEvent(Long event);

    List<Request> findRequestsByRequester(Long userId);

    List<Request> findRequestsByIdIn(List<Long> requestIds);
}
