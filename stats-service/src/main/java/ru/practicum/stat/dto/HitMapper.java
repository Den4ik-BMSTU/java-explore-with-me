package ru.practicum.stat.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stat.model.App;
import ru.practicum.stat.model.Hit;
import ru.practicum.stat.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HitMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appId", source = "appId")
    Hit toHit(CreateHitDto createHitDto, Long appId);

    @Mapping(target = "created", defaultValue = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "app", source = "appName")
    ResponseHitDto toResponseHitDto(Hit hit, String appName);

    @Mapping(target = "id", ignore = true)
    App toApp(String name);

    List<ResponseStatDto> toResponseStatDto(List<ViewStats> viewStats);
}
