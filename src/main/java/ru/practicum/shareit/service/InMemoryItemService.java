package ru.practicum.shareit.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.User;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InMemoryItemService implements ItemService {
    private final ItemMapper itemMapper;
    private final UserService userService;
    private final ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        if (itemDto == null) {
            throw new ValidationException("Тело запроса не может быть пустым");
        }
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Описание не может быть пустым");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Статус доступности обязателен");
        }

        User owner;
        try {
            owner = userService.getUserEntity(ownerId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Владелец с ID " + ownerId + " не найден");
        }

        // Создание и сохранение вещи
        Item item = Item.builder()
                .name(itemDto.getName().trim())
                .description(itemDto.getDescription().trim())
                .available(itemDto.getAvailable())
                .owner(owner)
                .build();


        item.setId(idCounter.getAndIncrement());
        items.put(item.getId(), item);

        log.info("Создана новая вещь: {}", item);
        return convertToDto(item);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        // 1. Проверка существования вещи
        Item existingItem = items.get(itemId);
        if (existingItem == null) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена");
        }

        // 2. Проверка прав владельца
        if (!existingItem.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Только владелец может редактировать вещь");
        }

        // 3. Частичное обновление полей
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName().trim());
        }

        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription().trim());
        }

        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }

        log.info("Обновлена вещь ID {}: {}", itemId, existingItem);

        return convertToDto(existingItem);
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

    private ItemDto convertToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(UserMapper.toUserDto(item.getOwner()))
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }
}