package ru.practicum.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers(List<Long> ids, long from, long size);

    UserDto saveUser(UserDto userDto);

    void removeUser(long userId);
}