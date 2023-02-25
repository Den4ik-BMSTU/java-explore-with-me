package ru.practicum.ewm.Compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.Compilations.dto.CompilationDto;
import ru.practicum.ewm.Compilations.dto.CompilationMapper;
import ru.practicum.ewm.Compilations.model.Compilation;
import ru.practicum.ewm.Compilations.repository.CompilationRepository;
import ru.practicum.ewm.Exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationPublicServiceImpl implements CompilationPublicService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> getAll(boolean pinned, Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from, size);
        List<Compilation> compilations = repository.findCompilationByPinned(pinned, page);
        log.info("Get all pinned={}, from={}, size={}", pinned, from, size);
        return compilations.stream()
                .map(mapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getById(Long compId) {
        Compilation compilation = repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation id=%s not found", compId)));
        log.info("Get compId={}", compId);
        return mapper.toCompilationDto(compilation);
    }
}
