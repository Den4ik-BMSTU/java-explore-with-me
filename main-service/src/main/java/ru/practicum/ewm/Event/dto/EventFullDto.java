package ru.practicum.ewm.Event.dto;

import lombok.*;
import ru.practicum.ewm.Categories.dto.CategoryDto;
import ru.practicum.ewm.Comments.dto.CommentShortDto;
import ru.practicum.ewm.Event.model.State;
import ru.practicum.ewm.Event.model.Location;
import ru.practicum.ewm.User.dto.UserShortDto;

import java.util.Set;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Integer views;
    private Set<CommentShortDto> comments;
}
