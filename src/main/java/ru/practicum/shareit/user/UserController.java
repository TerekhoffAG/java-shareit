package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.LogMessage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) {
        log.info(LogMessage.POST_REQUEST);
        return service.save(dto);
    }

    @PutMapping("/{userId}")
    public UserDto update(@RequestBody UserDto dto, @PathVariable Integer userId) {
        log.info(LogMessage.PUT_REQUEST);
        return service.update(dto, userId);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Integer id) {
        log.info(LogMessage.DELETE_REQUEST);
        service.delete(id);
    }

    @GetMapping("{id}")
    public UserDto getUserById(@PathVariable Integer id) {
        log.info(LogMessage.GET_REQUEST);
        return service.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info(LogMessage.GET_REQUEST);
        return service.getAllUsers();
    }
}
