package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.constant.LogMessage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto save(ItemDto dto, Integer userId) {
        User owner = userRepository.findOne(userId);
        Item item = ItemMapper.toItem(dto, owner, null);
        Item savedItem = itemRepository.create(item);
        log.info(LogMessage.CREATE_ITEM, savedItem.getId());

        return ItemMapper.toItemDto(savedItem);
    }

    public ItemDto update(ItemDto dto, Integer itemId, Integer userId) {
        dto.setId(itemId);
        Item updatedItem = itemRepository.update(dto, userId);
        log.info(LogMessage.UPDATE_ITEM, itemId);

        return ItemMapper.toItemDto(updatedItem);
    }

    public ItemDto findOne(Integer id) {
        Item item = itemRepository.findOne(id);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> findAllByUser(Integer userId) {
        userRepository.findOne(userId);
        return itemRepository.findAllByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> findFreeItemByKeyword(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.findFreeItemByKeyword(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
