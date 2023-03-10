package ru.practicum.Compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Event.*;
import ru.practicum.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final EventServiceImpl eventService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CompilationDto> getAllCompilations(boolean pinned, long from, long size) {
        return repository.findAll().stream()
                .filter(a -> a.getPinned() == pinned)
                .skip(from)
                .limit(size)
                .map(a -> CompilationMapper.toCompilationDto(a, getFullEventDtoList(repository.findAllEventIdsByCompilationId(a.getId()))))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(long compId) {
        compilationValid(compId);
        return CompilationMapper.toCompilationDto(repository.findCompilationById(compId),
                getFullEventDtoList(repository.findAllEventIdsByCompilationId(compId)));
    }

    @Transactional
    @Override
    public CompilationDto saveCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilation = repository.save(CompilationMapper.toCompilation(compilationNewDto));
        for (Long eventId : compilationNewDto.getEvents()) {
            String sql = "INSERT INTO EVENTS_COMPILATIONS(COMPILATION_ID, EVENT_ID) VALUES (?, ?) ";
            jdbcTemplate.update(sql, compilation.getId(), eventId);
        }
        return CompilationMapper.toCompilationDto(compilation, getFullEventDtoList(compilationNewDto.getEvents()));
    }

    @Transactional
    @Override
    public void removeCompilation(long compId) {
        compilationValid(compId);
        String sqlDelete = "DELETE FROM EVENTS_COMPILATIONS WHERE COMPILATION_ID = ? ";
        jdbcTemplate.update(sqlDelete, compId);
        repository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(long compId, CompilationDtoForUpdate compilationDtoForUpdate) {
        compilationValid(compId);
        Compilation compilation = repository.getById(compId);
        if (compilationDtoForUpdate.getPinned() == null) {
            compilationDtoForUpdate.setPinned(compilation.getPinned());
        }
        if (compilationDtoForUpdate.getTitle() == null) {
            compilationDtoForUpdate.setTitle(compilation.getTitle());
        }
        if (compilationDtoForUpdate.getEvents() == null) {
            compilationDtoForUpdate.setEvents(repository.findAllEventIdsByCompilationId(compId));
        } else {
            String sqlDelete = "DELETE FROM EVENTS_COMPILATIONS WHERE COMPILATION_ID = ? ";
            jdbcTemplate.update(sqlDelete, compId);
            if (compilationDtoForUpdate.getEvents().size() != 0) {
                for (Long eventId : compilationDtoForUpdate.getEvents()) {
                    String sqlInsert = "INSERT INTO EVENTS_COMPILATIONS(COMPILATION_ID, EVENT_ID) VALUES (?, ?) ";
                    jdbcTemplate.update(sqlInsert, compId, eventId);
                }
            }
        }
        Compilation newCompilation = CompilationMapper.toCompilation(compilationDtoForUpdate);
        newCompilation.setId(compId);
        return CompilationMapper.toCompilationDto(repository.save(newCompilation), getFullEventDtoList(compilationDtoForUpdate.getEvents()));
    }

    private List<EventFullDto> getFullEventDtoList(List<Long> eventIds) {
        return eventIds.stream()
                .map(eventRepository::getById)
                .map(eventService::toFullEventDtoAbsolutely)
                .collect(Collectors.toList());
    }

    private void compilationValid(long compId) {
        if (repository.findCompilationById(compId) == null) {
            log.error("???????????????? c id {} ???? ?????????????? ?????? ????????????????????", compId);
            throw new NotFoundException("???????????????? ???? ?????????????? ?????? ????????????????????");
        }
    }
}