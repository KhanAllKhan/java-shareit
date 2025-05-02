package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> createItem(
            @Valid @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        try {
            ItemDto createdItem = itemService.createItem(itemDto, ownerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        try {
            ItemDto updatedItem = itemService.updateItem(itemId, itemDto, ownerId);
            return ResponseEntity.ok(updatedItem);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getAllUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}