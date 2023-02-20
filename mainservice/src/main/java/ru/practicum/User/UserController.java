package ru.practicum.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exceptions.IncorrectParameterException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers(@RequestParam(value = "ids", required = false) List<Long> ids,
                                     @RequestParam(value = "from", defaultValue = "0") long from,
                                     @RequestParam(value = "size", defaultValue = "10") long size) {
        if (from < 0) {
            log.info("Неверный параметр from: {}, from должен быть больше или равен 0 ", from);
            throw new IncorrectParameterException("Неверный параметр from: {}, from должен быть больше или равен 0 " + from);
        }
        if (size <= 0) {
            log.info("Неверный параметр size: {}, size должен быть больше или равен 0 ", size);
            throw new IncorrectParameterException("Неверный параметр size: {}, size должен быть больше или равен 0 " + size);
        }
        log.info("Пользователи найдены");
        return userService.getAllUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Пользователь зарегистрирован");
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") long userId) {
        log.info("Пользователь удален");
        userService.removeUser(userId);
    }
}