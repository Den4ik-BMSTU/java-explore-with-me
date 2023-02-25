package ru.practicum.ewm.Event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.Categories.dto.CategoryDto;
import ru.practicum.ewm.User.dto.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto implements EventShort {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}
