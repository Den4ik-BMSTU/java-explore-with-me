package ru.practicum.stat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class RequestParametersException extends RuntimeException {
    private final String error;
}
