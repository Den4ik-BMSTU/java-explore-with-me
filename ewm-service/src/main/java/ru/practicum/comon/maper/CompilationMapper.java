package ru.practicum.comon.maper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comon.dto.CompilationDto;
import ru.practicum.comon.dto.EventShortDto;
import ru.practicum.comon.dto.NewCompilationDto;
import ru.practicum.comon.model.Compilation;
import ru.practicum.comon.model.Event;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationMapper {

    private final EventMapper eventMapper;

    public Compilation convertToCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new Compilation(
                null,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle(),
                events);
    }

    public CompilationDto convertToCompilationDto(Compilation compilation) {
        List<EventShortDto> eventsShortDto = eventMapper.convertAllToEventShortDto(compilation.getEvents());
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                eventsShortDto
        );
    }

    public List<CompilationDto> convertAllToCompilationDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(this::convertToCompilationDto)
                .collect(Collectors.toList());
    }

}
