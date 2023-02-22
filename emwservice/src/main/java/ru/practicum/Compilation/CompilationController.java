package ru.practicum.Compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Category.CategoryDto;
import ru.practicum.exceptions.IncorrectParameterException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAllCompilations(@RequestParam(value = "pinned") boolean pinned,
                                                   @RequestParam(value = "from", defaultValue = "0") long from,
                                                   @RequestParam(value = "size", defaultValue = "10") long size) {
        if (from < 0) {
            log.info("Неверный параметр from: {}, from должен быть больше или равен 0 ", from);
            throw new IncorrectParameterException("Неверный параметр from: {}, from должен быть больше или равен 0 " + from);
        }
        if (size <= 0) {
            log.info("Неверный параметр size: {}, size должен быть больше или равен 0 ", size);
            throw new IncorrectParameterException("Неверный параметр size: {}, size должен быть больше или равен 0 " + size);
        }
        log.info("Найдены подборки событий");
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getCompilationById(@PathVariable("compId") long compId) {
        log.info("Подборка событий найдена");
        return compilationService.getCompilationById(compId);
    }


    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("Подборка добавлена");
        return compilationService.saveCompilation(compilationNewDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable("compId") long compId) {
        log.info("Подборка удалена");
        compilationService.removeCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto updateCompilation(@PathVariable("compId") long compId,
                                            @Valid @RequestBody CompilationNewDto compilationNewDto) {
        log.info("Подборка обновлена");
        return compilationService.updateCompilation(compId, compilationNewDto);
    }
}