package ru.practicum.stat.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RequestParametersException extends RuntimeException {
    private final String error;
}
