package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseService handleNotFoundException(NotFoundException e) {
        log.warn("Error 404 {}", e.getMessage());
        return new ErrorResponseService(e.getError(), "NOT_FOUND");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseService handleExistingValidationException(ExistingValidationException e) {
        log.warn("Error 409 {}", e.getMessage());
        return new ErrorResponseService(e.getMessage(), "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseService handleValidation(final ValidationException e) {
        log.warn("Ошибка валидации 400 {}", e.getMessage());
        return new ErrorResponseService(e.getMessage(), "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseService handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> result = exception.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError ->
                                String.format("Ошибка Валидации '%s' значение = '%s'",
                                        fieldError.getField(), fieldError.getRejectedValue()),
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "")));
        log.warn("Ошибка 400 {}, {}", result, exception);
        return new ErrorResponseService(result.toString(), "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseService exc(ConstraintViolationException e) {
        log.warn("Ошибка валидации 409 {}", e.getMessage());
        return new ErrorResponseService(e.getMessage(), "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)  // BAD_REQUEST
    public ErrorResponseService handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        String message = exception.getMessage();
        Map<String, String> result = Map.of("Ошибка Request", Objects.isNull(message) ? "Неизвестно" : message);
        log.warn("Ошибка 409 {}, {}", result, exception);
        return new ErrorResponseService(message, "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseService handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {   // 400 BAD_REQUEST
        final String error = String.format("Unknown %s: %s", e.getName(), e.getValue());
        log.warn("Ошибка 400 Unknown {}: {}", e.getName(), e.getValue());
        return new ErrorResponseService(error, "BAD_REQUEST");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseService handleThrowable(final Throwable e) {
        log.warn("Error 409 {}", e.getMessage());
        return new ErrorResponseService(e.getLocalizedMessage(), "CONFLICT");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseService handleMissingParams(MissingServletRequestParameterException e) {
        log.warn("Error 400 {}", e.getMessage());
        return new ErrorResponseService(e.getMessage(), "BAD_REQUEST");
    }
}
