package ru.practicum.Compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Compilation findCompilationById(Long id);

    @Query(value = "SELECT EC.EVENT_ID " +
            "FROM EVENTS_COMPILATIONS AS ec " +
            "WHERE EC.COMPILATION_ID = ?1 ", nativeQuery = true)
    List<Long> findAllEventIdsByCompilationId(long compId);
}