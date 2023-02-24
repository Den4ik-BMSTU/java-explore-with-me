package ru.practicum.Compilation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.Event.EventFullDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventFullDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(events);
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());
        return compilationDto;
    }

    public static Compilation toCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationNewDto.getPinned());
        compilation.setTitle(compilationNewDto.getTitle());
        return compilation;
    }

    public static Compilation toCompilation(CompilationDtoForUpdate compilationDtoForUpdate) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDtoForUpdate.getPinned());
        compilation.setTitle(compilationDtoForUpdate.getTitle());
        return compilation;
    }
}