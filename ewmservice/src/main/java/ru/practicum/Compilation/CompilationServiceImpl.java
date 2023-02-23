package ru.practicum.Compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Category.CategoryMapper;
import ru.practicum.Category.CategoryRepository;
import ru.practicum.Event.Event;
import ru.practicum.Event.EventFullDto;
import ru.practicum.Event.EventMapper;
import ru.practicum.Event.EventRepository;
import ru.practicum.User.UserMapper;
import ru.practicum.User.UserRepository;
import ru.practicum.exceptions.IncorrectParameterException;
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
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
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
            String sql = "INSERT INTO events_compilations(compilation_id, event_id) VALUES (?, ?) ";
            jdbcTemplate.update(sql, compilation.getId(), eventId);
        }
        return CompilationMapper.toCompilationDto(compilation, getFullEventDtoList(compilationNewDto.getEvents()));
    }

    @Transactional
    @Override
    public void removeCompilation(long compId) {
        compilationValid(compId);
        String sqlDelete = "DELETE FROM events_compilations WHERE compilation_id = ? ";
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
            String sqlDelete = "DELETE FROM events_compilations WHERE compilation_id = ? ";
            jdbcTemplate.update(sqlDelete, compId);
            if (compilationDtoForUpdate.getEvents().size() != 0) {
                for (Long eventId : compilationDtoForUpdate.getEvents()) {
                    String sqlInsert = "INSERT INTO events_compilations(compilation_id, event_id) VALUES (?, ?) ";
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
                .map(this::toFullEventDtoAbsolutely)
                .collect(Collectors.toList());
    }

    private EventFullDto toFullEventDtoAbsolutely(Event event) {
        return EventMapper.toEventFullDto(event,
                CategoryMapper.toCategoryDto(categoryRepository.getById(event.getCategory())),
                UserMapper.toUserDto(userRepository.getById(event.getInitiatorId())));
    }

    private void compilationValid(long compId) {
        if (repository.findCompilationById(compId) == null) {
            log.error("Подборка c id {} не найдена или недоступна", compId);
            throw new NotFoundException("Подборка не найдена или недоступна");
        }
    }
}