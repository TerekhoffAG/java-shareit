package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.constant.ExpMessage;
import ru.practicum.shareit.exception.AlreadyFieldExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NullFieldModelException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idGenerator = 0;

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        int id = ++idGenerator;
        user.setId(id);
        users.put(id, user);

        return users.get(id);
    }

    @Override
    public User update(UserDto dto) {
        Integer id = dto.getId();
        String name = dto.getName();
        String email = dto.getEmail();

        checkEmail(email, id);
        checkUser(id);

        User oldUser = users.get(id);
        User newUser = new User(
                id,
                name != null ? name : oldUser.getName(),
                email != null ? email : oldUser.getEmail()
        );
        users.put(id, newUser);

        return users.get(id);
    }

    @Override
    public User findOne(Integer id) {
        checkUser(id);
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(Integer id) {
        checkUser(id);
        users.remove(id);
    }

    private void checkEmail(String email) {
        if (email == null) {
            throw new ValidationException(ExpMessage.INVALIDED_EMAIL);
        }
        List<User> list = findAll().stream()
                .filter(item -> item.getEmail().equals(email))
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            throw new AlreadyFieldExistsException(String.format(ExpMessage.EXISTS_EMAIL, email));
        }
    }

    private void checkEmail(String email, Integer id) {
        if (email != null) {
            if (!email.equals(findOne(id).getEmail())) {
                checkEmail(email);
            }
        }
    }

    private void checkUser(Integer id) {
        if (id == null) {
            throw new NullFieldModelException(ExpMessage.INVALIDED_USER_ID);
        }
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format(ExpMessage.NOT_FOUND_USER, id));
        }
    }
}
