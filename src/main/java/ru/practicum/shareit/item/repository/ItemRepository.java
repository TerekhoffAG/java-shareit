package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Item item);

    Item update(ItemDto dto, Integer userId);

    Item findOne(Integer id);

    List<Item> findAllByUser(Integer id);

    List<Item> findFreeItemByKeyword(String text);
}