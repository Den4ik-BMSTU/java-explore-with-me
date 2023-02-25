package ru.practicum.admyn.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.Compilation;

@Repository
public interface AdmCompilationRepository extends JpaRepository<Compilation, Long> {
}
