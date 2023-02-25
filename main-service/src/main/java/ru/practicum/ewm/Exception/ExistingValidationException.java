package ru.practicum.ewm.Exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExistingValidationException extends RuntimeException  {
    private final String message;
}
