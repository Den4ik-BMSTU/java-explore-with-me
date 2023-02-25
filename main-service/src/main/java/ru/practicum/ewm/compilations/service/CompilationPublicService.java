package ru.practicum.ewm.compilations.service;

import ru.practicum.ewm.compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {

    List<CompilationDto> getAll(boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compId);
}
