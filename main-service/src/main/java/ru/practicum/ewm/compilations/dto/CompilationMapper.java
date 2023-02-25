package ru.practicum.ewm.compilations.dto;

import org.mapstruct.*;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {EventMapper.class})
public interface CompilationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events")
    Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events);

    @Mapping(target = "events", source = "events")
    CompilationDto toCompilationDto(Compilation compilation);

    @Mapping(target = "events", source = "events")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Compilation updateCompilation(UpdateCompilationRequest updateCompilationRequest, @MappingTarget Compilation compilation, Set<Event> events);
}
