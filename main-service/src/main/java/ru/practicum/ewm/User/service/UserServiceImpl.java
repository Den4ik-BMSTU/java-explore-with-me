package ru.practicum.ewm.User.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.Exception.ExistingValidationException;
import ru.practicum.ewm.Exception.NotFoundException;
import ru.practicum.ewm.User.dto.NewUserDto;
import ru.practicum.ewm.User.dto.ResponseUserDto;
import ru.practicum.ewm.User.dto.UserMapper;
import ru.practicum.ewm.User.model.User;
import ru.practicum.ewm.User.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public ResponseUserDto add(NewUserDto newUserDto) {
        User user = userRepository.save(mapper.toUser(newUserDto));
        log.info("Add User DB user={}", user);
        return mapper.toResponseUserDto(user);
    }

    @Override
    public ResponseUserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s not found", userId)));
        log.info("user found {}", user);
        return mapper.toResponseUserDto(user);
    }

    @Override
    public List<ResponseUserDto> getAll(Integer from, Integer size, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            if (ids.stream().anyMatch(id -> id < 0)) {
                throw new ExistingValidationException("ids отрицательными элементами");
            }
            List<User> users = userRepository.findByIdIn(ids);
            return users.stream().map(mapper::toResponseUserDto).collect(Collectors.toList());
        }
        final PageRequest page = PageRequest.of(from, size);
        Page<User> users = userRepository.findAll(page);
        return users.stream()
                .map(mapper::toResponseUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void remove(Long userId) {
        isExistsUserById(userId);
        userRepository.deleteById(userId);
        log.info("Delete User id={}", userId);

    }

    private void isExistsUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s not found", id)));
    }
}
