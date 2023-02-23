package ru.practicum.ewm.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExistingValidationException extends RuntimeException  {
    private final String message;
}
