package ru.practicum.ewm.Compilations.dto;

import org.mapstruct.*;
import ru.practicum.ewm.Compilations.model.Compilation;
import ru.practicum.ewm.Event.dto.EventMapper;
import ru.practicum.ewm.Event.model.Event;

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
