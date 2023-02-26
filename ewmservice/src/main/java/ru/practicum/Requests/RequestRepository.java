package ru.practicum.Requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findRequestById(Long id);

    @Query(value = "select r.* " +
            "from requests as r " +
            "where r.requester_id = ?1 ", nativeQuery = true)
    List<Request> findRequestsByUserId(long userId);

    @Query(value = "select r.* " +
            "from requests as r " +
            "where r.requester_id = ?1 and r.event_id = ?2 ", nativeQuery = true)
    List<Request> findRequestsByUserIdAndByEventId(long userId, long eventId);

    @Query(value = "select r.* " +
            "from requests as r " +
            "left join events as e on r.event_id = e.id " +
            "where e.initiator_id = ?1 and r.event_id = ?2 ", nativeQuery = true)
    List<Request> findAllByOwnerIdAndByEventId(long userId, long eventId);
}