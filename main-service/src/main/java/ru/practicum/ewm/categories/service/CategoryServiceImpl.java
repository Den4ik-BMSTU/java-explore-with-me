package ru.practicum.ewm.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.categories.dto.*;
import ru.practicum.ewm.categories.model.Category;
import ru.practicum.ewm.categories.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.ExistingValidationException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public ResponseCategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategoryDto));
        log.info("Add Category={}", category);
        return categoryMapper.toResponseCategoryDto(category);
    }

    @Override
    @Transactional
    public ResponseCategoryDto update(NewCategoryDto categoryDto, Long catId) {
        isExistsCategoryById(catId);

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", catId)));
        category.setName(categoryDto.getName());
        log.info("Update Category={}", category);
        return categoryMapper.toResponseCategoryDto(category);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        isExistsCategoryById(id);
        if (eventRepository.existsByCategory_Id(id)) {
            throw new ExistingValidationException("Категория не доступна для удаления");
        }
        // Обратите внимание: с категорией не должно быть связано ни одного события.
        log.info("Delete CategoryId={}", id);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<ResponseCategoryDto> getAll(Integer from, Integer size) {
        final PageRequest page = PageRequest.of(from, size);
        Page<Category> categories = categoryRepository.findAll(page);
        return categories.stream()
                .map(categoryMapper::toResponseCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseCategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", id)));
        log.info("Get CategoryId={}", id);
        return categoryMapper.toResponseCategoryDto(category);
    }

    private void isExistsCategoryById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category id=%s not found", id)));
    }
}
