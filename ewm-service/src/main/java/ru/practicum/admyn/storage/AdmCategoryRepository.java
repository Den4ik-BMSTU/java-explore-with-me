package ru.practicum.admyn.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.Category;

@Repository
public interface AdmCategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
}
