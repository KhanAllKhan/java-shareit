package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.ItemClient;
import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item {}, ownerId={}", itemDto, ownerId);
        return itemClient.createItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                            @PathVariable("itemId") Long itemId,
                                            @RequestBody @Valid ItemDto itemDto) {
        log.info("Updating item {}, ownerId={}", itemId, ownerId);
        return itemClient.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @PathVariable("itemId") Long itemId) {
        log.info("Getting item {}, ownerId={}", itemId, ownerId);
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting items for ownerId={}, from={}, size={}", ownerId, from, size);
        return itemClient.getUserItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestParam(name = "text") String text,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Searching items with text '{}', from={}, size={}", text, from, size);
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long authorId,
            @PathVariable("itemId") Long itemId,
            @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(authorId, itemId, commentDto);
    }
}