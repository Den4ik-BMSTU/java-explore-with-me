package ru.practicum.ewm.Compilations.service;

import ru.practicum.ewm.Compilations.dto.CompilationDto;
import ru.practicum.ewm.Compilations.dto.NewCompilationDto;
import ru.practicum.ewm.Compilations.dto.UpdateCompilationRequest;

public interface CompilationAdminService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    CompilationDto updateByPin(Long compId, UpdateCompilationRequest updateCompilationRequest);

    void removeById(Long compId);
}
