package ru.practicum.ewm.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUpdateCommentDto {

    private Long id;
    @NotBlank
    @Size(min = 10, max = 5000)
    private String text;
}
