package ru.practicum.ewm.categories.service;

import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.dto.ResponseCategoryDto;

import java.util.List;

public interface CategoryService {
    ResponseCategoryDto add(NewCategoryDto newCategoryDto);

    ResponseCategoryDto update(NewCategoryDto categoryDto, Long id);

    void remove(Long id);

    List<ResponseCategoryDto> getAll(Integer from, Integer size);

    ResponseCategoryDto getById(Long id);
}
