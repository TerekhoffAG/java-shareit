package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    Item update(ItemDto dto);

    Item findOne(Integer id);

    List<Item> findAll();

    void delete(Integer id);
}
