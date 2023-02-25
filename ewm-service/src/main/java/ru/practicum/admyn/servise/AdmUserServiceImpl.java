package ru.practicum.admyn.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admyn.storage.AdmUserRepository;
import ru.practicum.comon.dto.UserDto;
import ru.practicum.comon.exception.EntityNoAccessException;
import ru.practicum.comon.exception.EntityNotFoundException;
import ru.practicum.comon.maper.UserMapper;
import ru.practicum.comon.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmUserServiceImpl implements AdmUserService {

    private final AdmUserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new EntityNoAccessException("Email must be unique");
        }
        User createdUser = repository.save(mapper.convertToUser(userDto));
        return mapper.convertToUserDto(createdUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("This user not founded");
        }
        repository.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Pageable pageable) {
        List<User> users;
        if (ids.isEmpty()) {
            users = repository.findAll(pageable).toList();
        } else {
            users = repository.findAllByIdIn(ids, pageable);
        }
        return mapper.convertAllToUserDto(users);
    }
}
