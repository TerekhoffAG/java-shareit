package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.constant.ExpMessage;
import ru.practicum.shareit.exception.NullFieldModelException;
import ru.practicum.shareit.exception.PermissionException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private Integer idGenerator = 0;

    @Override
    public Item create(Item item) {
        int id = ++idGenerator;
        item.setId(id);
        items.put(id, item);

        return items.get(id);
    }

    @Override
    public Item update(ItemDto dto) {
        Integer itemId = dto.getId();
        Integer ownerId = dto.getOwner().getId();
        Item oldItem = items.get(itemId);

        checkItem(ownerId);
        checkOwner(oldItem, ownerId);

        return items.get(itemId);
    }

    @Override
    public Item findOne(Integer id) {
        return null;
    }

    @Override
    public List<Item> findAll() {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    private void checkItem(Integer id) {
        if (!items.containsKey(id)) {
            throw new NullFieldModelException(String.format(ExpMessage.NOT_FOUND_ITEM, id));
        }
    }

    private void checkOwner(Item item, Integer ownerId) {
        if (item.getOwner().getId() != ownerId) {
            throw new PermissionException(ExpMessage.UPDATE_ANOTHER_USER_ITEM);
        }
    }
}
