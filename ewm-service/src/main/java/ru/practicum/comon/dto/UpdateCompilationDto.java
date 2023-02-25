package ru.practicum.comon.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateCompilationDto {

    private Boolean pinned;

    private String title;

    private List<Long> events;
}
