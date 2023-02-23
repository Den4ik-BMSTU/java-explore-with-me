package ru.practicum.ewm.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private Set<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(max = 100)
    private String title;
}
