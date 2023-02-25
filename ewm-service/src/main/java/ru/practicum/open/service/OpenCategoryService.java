package ru.practicum.open.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.comon.dto.CategoryDto;

import java.util.List;

public interface OpenCategoryService {
    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(Long id);
}
