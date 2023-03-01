package ru.practicum.Requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getAllRequestsOfUser(@PathVariable long userId) {
        log.info("Найдены запросы на участие");
        return requestService.getAllRequestsOfUser(userId);
    }

    @GetMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> getAllRequestsByOwnerIdAndByEventId(@PathVariable long userId,
                                                                @PathVariable long eventId) {
        log.info("Найдены запросы на участие");
        return requestService.getAllRequestsByOwnerIdAndByEventId(userId, eventId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveRequest(@PathVariable long userId,
                                  @RequestParam(value = "eventId", required = false) Long eventId) {
        log.info("Заявка создана");
        return requestService.saveRequest(userId, eventId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto cancelRequest(@PathVariable long userId,
                                    @PathVariable long requestId) {
        log.info("Заявка отменена");
        return requestService.cancelRequest(userId, requestId);
    }

    @PatchMapping("/events/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestListStatusUpdateResult updateRequestsStatus(@PathVariable long userId,
                                                              @PathVariable long eventId,
                                                              @Valid @RequestBody RequestListStatusUpdate requestListStatusUpdate) {
        log.info("Статус заявок изменён");
        return requestService.updateRequestsStatus(userId, eventId, requestListStatusUpdate);
    }
}