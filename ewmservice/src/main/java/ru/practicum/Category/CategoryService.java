package ru.practicum.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(long from, long size);

    CategoryDto getCategoryById(long catId);

    CategoryDto saveCategory(CategoryDto categoryDto);

    void removeCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}