package ru.practicum.open.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.comon.model.Category;

@Repository
public interface OpenCategoryRepository extends JpaRepository<Category, Long> {
}
