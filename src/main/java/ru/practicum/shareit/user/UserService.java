package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.constant.LogMessage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDto save(UserDto dto) {
        User user = repository.create(UserMapper.toUser(dto));
        log.info(LogMessage.CREATE_USER, user.getId());
        return UserMapper.toUserDto(user);
    }

    public UserDto update(UserDto dto, Integer id) {
        dto.setId(id);
        User user = repository.update(dto);
        log.info(LogMessage.UPDATE_USER, id);
        return UserMapper.toUserDto(user);
    }

    public void delete(Integer id) {
        repository.delete(id);
        log.info(LogMessage.REMOVE_USER, id);
    }

    public UserDto getUserById(Integer id) {
        User user = repository.findOne(id);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
