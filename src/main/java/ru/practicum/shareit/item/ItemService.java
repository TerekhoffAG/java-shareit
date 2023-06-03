package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.constant.ExpMessage;
import ru.practicum.shareit.constant.LogMessage;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    public ItemDto save(ItemDto dto, Integer userId) {
        User owner = userService.getUserEntity(userId);
        Item item = ItemMapper.toItem(dto, owner, null);
        Item savedItem = itemRepository.save(item);
        log.info(LogMessage.CREATE_ITEM, savedItem.getId());

        return ItemMapper.toItemDto(savedItem);
    }

    public ItemDto update(ItemDto dto, Integer itemId, Integer userId) {
        String name = dto.getName();
        String description = dto.getDescription();
        Boolean available = dto.getAvailable();

        Item item = getItemEntity(itemId);
        Integer ownerId = userService.getUserEntity(userId).getId();

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException(ExpMessage.UPDATE_ANOTHER_USER_ITEM);
        }
        if (name != null) {
            item.setName(name);
        }
        if (description != null) {
            item.setDescription(description);
        }
        if (available != null) {
            item.setAvailable(available);
        }

        Item updatedItem = itemRepository.save(item);
        log.info(LogMessage.UPDATE_ITEM, itemId);

        return ItemMapper.toItemDto(updatedItem);
    }

    public ItemDto findOne(Integer id) {
        Item item = getItemEntity(id);

        return ItemMapper.toItemDto(item);
    }

    public Item getItemEntity(Integer id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(ExpMessage.NOT_FOUND_ITEM, id)));
    }

    public List<ItemDto> findAllByUser(Integer userId) {
        User user = userService.getUserEntity(userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> findFreeItemByKeyword(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.findByNameOrDescription(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
