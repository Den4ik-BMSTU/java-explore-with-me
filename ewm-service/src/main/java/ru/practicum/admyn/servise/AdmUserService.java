package ru.practicum.admyn.servise;

import org.springframework.data.domain.Pageable;
import ru.practicum.comon.dto.UserDto;

import java.util.List;

public interface AdmUserService {
    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getUsers(List<Long> ids, Pageable pageable);
}
