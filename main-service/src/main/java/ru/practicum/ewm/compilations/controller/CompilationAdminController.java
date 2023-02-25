package ru.practicum.ewm.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilations.dto.CompilationDto;
import ru.practicum.ewm.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilations.service.CompilationAdminService;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationAdminService compilationAdminService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.info("Admin create Compilation={}", newCompilationDto);
        return compilationAdminService.create(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeById(@PathVariable Long compId) {
        log.info("Admin delete compId={}", compId);
        compilationAdminService.removeById(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateByPin(@PathVariable Long compId,
                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.info("Update Compilation compId={}", compId);
        return compilationAdminService.updateByPin(compId, updateCompilationRequest);
    }
}
