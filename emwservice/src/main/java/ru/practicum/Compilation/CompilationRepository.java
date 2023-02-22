package ru.practicum.Compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.Event.Event;
import ru.practicum.Requests.Request;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Compilation findCompilationById(Long id);
    @Query(value = "select ec.event_id " +
            "from events_compilations as ec " +
            "where ec.compilation_id = ?1 ", nativeQuery = true)
    List<Long> findAllEventIdsByCompilationId(long compId);
}