package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constant.LogMessage;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto dto, @RequestHeader(USER_ID) Integer userId) {
        log.info(LogMessage.POST_REQUEST);
        return itemService.save(dto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto dto, @PathVariable Integer itemId, @RequestHeader(USER_ID) Integer userId) {
        log.info(LogMessage.PATCH_REQUEST);
        return itemService.update(dto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Integer itemId) {
        log.info(LogMessage.GET_REQUEST);
        return itemService.findOne(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllByUser(@RequestHeader(USER_ID) Integer userId) {
        log.info(LogMessage.GET_REQUEST);
        return itemService.findAllByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getFreeItemByKeyword(@RequestParam String text) {
        log.info(LogMessage.GET_REQUEST);
        return itemService.findFreeItemByKeyword(text);
    }
}
