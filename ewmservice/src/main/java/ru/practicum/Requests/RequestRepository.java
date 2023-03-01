package ru.practicum.Requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findRequestById(Long id);

    @Query(value = "SELECT R.* " +
            "FROM REQUESTS AS R " +
            "WHERE R.REQUESTER_ID = ?1 ", nativeQuery = true)
    List<Request> findRequestsByUserId(long userId);

    @Query(value = "SELECT R.* " +
            "FROM REQUESTS AS R " +
            "WHERE R.REQUESTER_ID = ?1 AND R.EVENT_ID = ?2 ", nativeQuery = true)
    List<Request> findRequestsByUserIdAndByEventId(long userId, long eventId);

    @Query(value = "SELECT R.* " +
            "FROM REQUESTS AS R " +
            "LEFT JOIN EVENTS AS E ON R.EVENT_ID = E.id " +
            "WHERE E.INITIATOR_ID = ?1 AND R.EVENT_ID = ?2 ", nativeQuery = true)
    List<Request> findAllByOwnerIdAndByEventId(long userId, long eventId);
}