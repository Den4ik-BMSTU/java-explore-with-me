package ru.practicum.ewm.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Name - не заполнен")
    @Size(max = 50)
    private String name;
}
