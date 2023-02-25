package ru.practicum.admyn.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.Location;

@Repository
public interface AdmLocationRepository extends JpaRepository<Location, Long> {
}
