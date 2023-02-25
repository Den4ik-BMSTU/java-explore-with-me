package ru.practicum.admyn.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.admyn.storage.AdmCategoryRepository;
import ru.practicum.admyn.storage.AdmEventRepository;
import ru.practicum.comon.dto.CategoryDto;
import ru.practicum.comon.exception.EntityNoAccessException;
import ru.practicum.comon.exception.EntityNotFoundException;
import ru.practicum.comon.maper.CategoryMapper;
import ru.practicum.comon.model.Category;

@Service
@RequiredArgsConstructor
public class AdmCategoryServiceImpl implements AdmCategoryService {

    private final AdmCategoryRepository categoryRepository;
    private final AdmEventRepository eventRepository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new EntityNoAccessException("Category name must be unique");
        }

        Category savedCategory = categoryRepository.save(mapper.convertToCategory(categoryDto));
        return mapper.convertToCategoryDto(savedCategory);
    }

    @Override
    public void deleteCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new EntityNotFoundException("Category not founded");
        }
        if (eventRepository.existsByCategory_Id(catId)) {
            throw new EntityNoAccessException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        if (!categoryRepository.existsById(catId)) {
            throw new EntityNotFoundException("Category not founded");
        }
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new EntityNoAccessException("Category name must be unique");
        }
        Category updatedCategory = categoryRepository.save(new Category(catId, categoryDto.getName()));
        return mapper.convertToCategoryDto(updatedCategory);
    }
}
