package ru.practicum.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, long from, long size) {
        if (ids.size() == 0){
            return UserMapper.toListUserDto(repository.findAll().stream()
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList()));
        } else {
            List<UserDto> userDtos = new ArrayList<>();
            for (Long id : ids) {
                userValid(id);
                userDtos.add(UserMapper.toUserDto(repository.findUserById(id)));
            }
            return userDtos.stream()
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) {
        emailValid(userDto.getEmail());
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(userDto)));
    }

    @Transactional
    @Override
    public void removeUser(long userId) {
        userValid(userId);
        repository.deleteById(userId);
    }

    private void emailValid(String email) {
        if (repository.findUserByEmail(email) != null) {
            log.error("Пользователь c email уже существует {}", email);
            throw new ConflictException("Пользователь уже существует");
        }
    }

    private void userValid(long userId) {
        if (repository.findUserById(userId) == null) {
            log.error("Пользователь c userId не найден или недоступен {}", userId);
            throw new NotFoundException("Пользователь не найден или недоступен");
        }
    }
}