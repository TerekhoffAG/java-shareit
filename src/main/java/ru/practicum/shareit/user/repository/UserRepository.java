package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserRepository {
    User create(User user);
    User update(User user);
    User findOne(Integer id);
    List<User> findAll();
    void delete(Integer id);
}
