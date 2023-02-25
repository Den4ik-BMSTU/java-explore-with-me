package ru.practicum.ewm.Compilations.service;

import ru.practicum.ewm.Compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationDto> getAll(boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);
}
