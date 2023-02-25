package ru.practicum.Requests;

import java.util.List;

public interface RequestService {
    List<RequestDto> getAllRequestsOfUser(long userId);

    List<RequestDto> getAllRequestsByOwnerIdAndByEventId(long userId, long eventId);

    RequestDto saveRequest(long userId, Long eventId);

    RequestDto cancelRequest(long userId, long requestId);

    RequestListStatusUpdateResult updateRequestsStatus(long userId, long eventId,
                                                       RequestListStatusUpdate requestListStatusUpdate);
}