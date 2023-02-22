package ru.practicum.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.User.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryById(Long id);
    Category findCategoryByName(String name);
}