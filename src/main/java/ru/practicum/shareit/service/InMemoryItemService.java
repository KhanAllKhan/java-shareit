package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.User;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InMemoryItemService implements ItemService {
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userService.getUserEntity(ownerId);
        Item item = itemMapper.toItem(itemDto);
        item.setId(idCounter.getAndIncrement());
        item.setOwner(owner);
        items.put(item.getId(), item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        Item existingItem = items.get(itemId);
        if (existingItem == null) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена");
        }

        if (!existingItem.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Только владелец может редактировать вещь");
        }

        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        return itemMapper.toItemDto(existingItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена");
        }
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllUserItems(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(ownerId))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String searchText = text.toLowerCase().trim();

        return items.values().stream()
                .filter(Item::isAvailable) // Используем method reference
                .filter(item -> {
                    String name = item.getName() != null ? item.getName().toLowerCase() : "";
                    String description = item.getDescription() != null ? item.getDescription().toLowerCase() : "";
                    return name.contains(searchText) || description.contains(searchText);
                })
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}