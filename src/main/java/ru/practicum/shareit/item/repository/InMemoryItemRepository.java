package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.constant.ExpMessage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NullFieldModelException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

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
    public Item update(ItemDto dto, Integer userId) {
        Integer itemId = dto.getId();
        String name = dto.getName();
        String description = dto.getDescription();
        Boolean available = dto.getAvailable();

        checkItem(itemId);
        Item oldItem = items.get(itemId);
        checkOwner(oldItem, userId);

        Item newItem = new Item(
                itemId,
                name != null ? name : oldItem.getName(),
                description != null ? description : oldItem.getDescription(),
                available != null ? available : oldItem.getAvailable(),
                oldItem.getOwner(),
                oldItem.getRequest()
        );
        items.put(itemId, newItem);

        return items.get(itemId);
    }

    @Override
    public Item findOne(Integer id) {
        checkItem(id);
        return items.get(id);
    }

    @Override
    public List<Item> findAllByUser(Integer userId) {
        checkItem(userId);
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findFreeItemByKeyword(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    private void checkItem(Integer id) {
        if (!items.containsKey(id)) {
            throw new NullFieldModelException(String.format(ExpMessage.NOT_FOUND_ITEM, id));
        }
    }

    private void checkOwner(Item item, Integer ownerId) {
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException(ExpMessage.UPDATE_ANOTHER_USER_ITEM);
        }
    }
}
