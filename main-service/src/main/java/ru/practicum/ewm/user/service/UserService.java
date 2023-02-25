package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.ResponseUserDto;

import java.util.List;

public interface UserService {

    ResponseUserDto add(NewUserDto newUserDto);

    ResponseUserDto getById(Long userId);

    List<ResponseUserDto> getAll(Integer from, Integer size, List<Long> ids);

    void remove(Long userId);
}
