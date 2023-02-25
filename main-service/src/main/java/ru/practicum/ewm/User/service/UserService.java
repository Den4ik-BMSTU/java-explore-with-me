package ru.practicum.ewm.User.service;

import ru.practicum.ewm.User.dto.NewUserDto;
import ru.practicum.ewm.User.dto.ResponseUserDto;

import java.util.List;

public interface UserService {

    ResponseUserDto add(NewUserDto newUserDto);

    ResponseUserDto getById(Long userId);

    List<ResponseUserDto> getAll(Integer from, Integer size, List<Long> ids);

    void remove(Long userId);
}
