package ru.practicum.exception;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiError {
    private StackTraceElement[] errors;
    private String message;
    private String reason;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
