package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserRepository {
    User create(User user);

    User update(UserDto dto);

    User findOne(Integer id);

    List<User> findAll();

    void delete(Integer id);
}
