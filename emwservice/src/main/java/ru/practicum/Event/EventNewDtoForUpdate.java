package ru.practicum.Event;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventNewDtoForUpdate {
   String annotation;

    Long category;

    String description;

    String eventDate;

    Location location;

    Boolean paid;

    Long participantLimit;

    Boolean requestModeration;

    String title;
    String stateAction;
}