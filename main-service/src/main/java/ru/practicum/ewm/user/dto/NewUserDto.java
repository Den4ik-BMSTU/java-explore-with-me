package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {
    @NotBlank(message = "Name - не заполнен")
    @Size(max = 70)
    private String name;
    @Email(message = "Не верный формат Email")
    @NotBlank(message = "Email - не заполнен")
    @Size(max = 70)
    private String email;
}
