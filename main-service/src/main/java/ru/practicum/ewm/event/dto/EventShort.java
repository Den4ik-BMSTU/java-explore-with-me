package ru.practicum.ewm.event.dto;

import ru.practicum.ewm.categories.dto.CategoryDto;
import ru.practicum.ewm.user.dto.UserShortDto;

public interface EventShort {

    Long getId();

    String getAnnotation();

    CategoryDto getCategory();

    Integer getConfirmedRequests();

    String getEventDate();

    UserShortDto getInitiator();

    Boolean getPaid();

    String getTitle();

    Integer getViews();
}
