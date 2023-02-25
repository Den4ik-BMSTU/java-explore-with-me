package ru.practicum.persanal.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.EventRequest;

import java.util.List;

@Repository
public interface PersonalRequestRepository extends JpaRepository<EventRequest, Long> {

    List<EventRequest> findAllByRequester_Id(Long userId);

    List<EventRequest> findAllByIdIn(List<Long> requestId);

}
