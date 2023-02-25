package ru.practicum.ewm.Event.dto;

import ru.practicum.ewm.Categories.dto.CategoryDto;
import ru.practicum.ewm.User.dto.UserShortDto;

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
