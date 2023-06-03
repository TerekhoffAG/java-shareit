package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.constant.ExpMessage;
import ru.practicum.shareit.constant.LogMessage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserDto save(UserDto dto) {
        User user = repository.save(UserMapper.toUser(dto));
        log.info(LogMessage.CREATE_USER, user.getId());

        return UserMapper.toUserDto(user);
    }

    public UserDto update(UserDto dto, Integer id) {
        User user = getUserEntity(id);
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        User updatedUser = repository.save(user);
        log.info(LogMessage.UPDATE_USER, id);

        return UserMapper.toUserDto(updatedUser);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
        log.info(LogMessage.REMOVE_USER, id);
    }

    public UserDto findOne(Integer id) {
        User user = getUserEntity(id);

        return UserMapper.toUserDto(user);
    }

    public User getUserEntity(Integer id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(ExpMessage.NOT_FOUND_USER, id)));
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
