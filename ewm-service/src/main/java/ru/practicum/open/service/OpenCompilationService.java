package ru.practicum.open.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.comon.dto.CompilationDto;

import java.util.List;

public interface OpenCompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable);

    CompilationDto getCompilationById(Long compId);
}
