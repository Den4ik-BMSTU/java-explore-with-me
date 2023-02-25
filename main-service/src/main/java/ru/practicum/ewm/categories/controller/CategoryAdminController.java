package ru.practicum.ewm.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.NewCategoryDto;
import ru.practicum.ewm.categories.dto.ResponseCategoryDto;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseCategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Crete Category={}", newCategoryDto);
        return categoryService.add(newCategoryDto);
    }

    @PatchMapping("/{catId}")
    public ResponseCategoryDto update(@PathVariable @Positive Long catId,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Update Category={}, catId={}", newCategoryDto, catId);
        return categoryService.update(newCategoryDto, catId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        log.info("Remove CategoryID={}", id);
        categoryService.remove(id);
    }
}
