package ru.practicum.admyn.servise;

import ru.practicum.comon.dto.CompilationDto;
import ru.practicum.comon.dto.NewCompilationDto;
import ru.practicum.comon.dto.UpdateCompilationDto;

public interface AdmCompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateCompilationDto);
}
