package ru.practicum.ewm.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.categories.dto.ResponseCategoryDto;
import ru.practicum.ewm.categories.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public List<ResponseCategoryDto> getAll(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("GetAll Category from={}, size={}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{id}")
    public ResponseCategoryDto getById(@PathVariable Long id) {
        log.info("Get Category Id={}", id);
        return categoryService.getById(id);
    }
}
