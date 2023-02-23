package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.State;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {
    private Long id;
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // "yyyy-MM-dd HH:mm:ss"
    private Location location;   // координаты необходимо изменить тип
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private State stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
