package ru.practicum.Requests;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.User.UserDto;

import javax.validation.Valid;
import java.util.List;

public interface RequestService {
    List<RequestDto> getAllRequestsOfUser(long userId);

    List<RequestDto> getAllRequestsByOwnerIdAndByEventId(long userId, long eventId);

    RequestDto saveRequest(long userId, long eventId);

    RequestDto cancelRequest(long userId, long requestId);

    RequestListStatusUpdateResult updateRequestsStatus(long userId, long eventId,
                                                       RequestListStatusUpdate requestListStatusUpdate);
}