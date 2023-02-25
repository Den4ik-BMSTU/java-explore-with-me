package ru.practicum.open.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comon.dto.CompilationDto;
import ru.practicum.comon.exception.EntityNotFoundException;
import ru.practicum.comon.maper.CompilationMapper;
import ru.practicum.comon.model.Compilation;
import ru.practicum.comon.model.QCompilation;
import ru.practicum.open.storage.OpenCompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenCompilationServiceImpl implements OpenCompilationService {

    private final OpenCompilationRepository compilationRepository;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Pageable pageable) {
        QCompilation qCompilation = QCompilation.compilation;
        BooleanExpression predicate = qCompilation.isNotNull();

        if (pinned != null) {
            predicate = predicate.and(qCompilation.pinned.eq(pinned));
        }
        List<Compilation> compilations = compilationRepository.findAll(predicate, pageable).toList();
        return mapper.convertAllToCompilationDto(compilations);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation not founded"));
        return mapper.convertToCompilationDto(compilation);

    }
}
