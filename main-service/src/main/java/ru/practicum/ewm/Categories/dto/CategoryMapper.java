package ru.practicum.ewm.Categories.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.Categories.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto category);

    CategoryDto toCategoryDto(Category category);

    ResponseCategoryDto toResponseCategoryDto(Category category);
}
