package ru.practicum.Category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.User.UserDto;
import ru.practicum.exceptions.IncorrectParameterException;

import javax.validation.Valid;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(long from, long size);

    CategoryDto getCategoriyById(long catId);

    CategoryDto saveCategory(CategoryDto categoryDto);

    void removeCategory(long catId);

    CategoryDto updateCategory(long catId, CategoryDto categoryDto);
}