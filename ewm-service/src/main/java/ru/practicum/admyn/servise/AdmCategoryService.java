package ru.practicum.admyn.servise;

import ru.practicum.comon.dto.CategoryDto;

public interface AdmCategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);
}
