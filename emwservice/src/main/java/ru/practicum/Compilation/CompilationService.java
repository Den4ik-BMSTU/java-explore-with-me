package ru.practicum.Compilation;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(boolean pinned, long from, long size);

    CompilationDto getCompilationById(long compId);

    CompilationDto saveCompilation(CompilationNewDto compilationNewDto);

    void removeCompilation(long compId);

    CompilationDto updateCompilation(long compId, CompilationNewDto compilationNewDto);
}