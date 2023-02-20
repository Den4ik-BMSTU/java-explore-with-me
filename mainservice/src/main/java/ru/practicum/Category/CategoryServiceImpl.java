package ru.practicum.Category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.User.UserDto;
import ru.practicum.User.UserMapper;
import ru.practicum.User.UserRepository;
import ru.practicum.User.UserService;
import ru.practicum.Event.Event;
import ru.practicum.Event.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;
    @Override
    public List<CategoryDto> getAllCategories(long from, long size) {
        return CategoryMapper.toListCategoryDto(repository.findAll()).stream()
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoriyById(long catId) {
        categoryValid(catId);
        return CategoryMapper.toCategoryDto(repository.getById(catId));
    }

    @Transactional
    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        nameValid(categoryDto.getName());
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(categoryDto)));
    }

    @Transactional
    @Override
    public void removeCategory(long catId) {
        categoryValid(catId);
        for (Event event : eventRepository.findAll()) {
            if (event.getCategory() == catId){
                log.error("Существуют события, связанные с категорией {}", catId);
                throw new ConflictException("Существуют события, связанные с категорией");
            }
        }
        repository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        categoryValid(catId);
        nameValid(categoryDto.getName());
        Category category = CategoryMapper.toCategory(categoryDto);
        category.setId(catId);
        return CategoryMapper.toCategoryDto(repository.save(category));
    }

    private void categoryValid(long catId) {
        if (repository.findCategoryById(catId) == null) {
            log.error("Категория с {} не найдена или недоступна", catId);
            throw new NotFoundException("Категория не найдена или недоступна");
        }
    }

    private void nameValid(String name) {
        if (repository.findCategoryByName(name) != null) {
            log.error("Категория c {} уже существует", name);
            throw new ConflictException("Категория уже существует");
        }
    }
}