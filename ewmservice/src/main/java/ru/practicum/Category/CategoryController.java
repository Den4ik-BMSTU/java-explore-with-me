package ru.practicum.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.IncorrectParameterException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAllCategories(@RequestParam(value = "from", defaultValue = "0") long from,
                                              @RequestParam(value = "size", defaultValue = "10") long size) {
        if (from < 0) {
            log.info("Неверный параметр from: {}, from должен быть больше или равен 0 ", from);
            throw new IncorrectParameterException("Неверный параметр from: {}, from должен быть больше или равен 0 " + from);
        }
        if (size <= 0) {
            log.info("Неверный параметр size: {}, size должен быть больше или равен 0 ", size);
            throw new IncorrectParameterException("Неверный параметр size: {}, size должен быть больше или равен 0 " + size);
        }
        log.info("Категории найдены");
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("Категория найдена");
        return categoryService.getCategoryById(catId);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto saveCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Категория добавлена");
        return categoryService.saveCategory(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable long catId) {
        log.info("Категория удалена");
        categoryService.removeCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable long catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Данные категории изменены");
        return categoryService.updateCategory(catId, categoryDto);
    }
}