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
        User owner = userRepository.findOne(userId);
        dto.setId(itemId);
        dto.setOwner(owner);
        Item updatedItem = itemRepository.update(dto);
        log.info(LogMessage.UPDATE_ITEM, itemId);

        return ItemMapper.toItemDto(updatedItem);
    }

    public ItemDto findOne(Integer id) {
        Item item = itemRepository.findOne(id);
        return ItemMapper.toItemDto(item);
    }

    public List<ItemDto> findAllByUser(Integer userId) {
        return itemRepository.findAll().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
