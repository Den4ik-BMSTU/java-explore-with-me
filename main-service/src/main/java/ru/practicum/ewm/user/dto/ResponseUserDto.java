package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDto {
    private Long id;
    private String name;
    private String email;
}
