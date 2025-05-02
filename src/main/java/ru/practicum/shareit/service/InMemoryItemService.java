package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class InMemoryItemService implements ItemService {
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userService.getUserEntity(ownerId);  // Получаем User, а не UserDto
        Item item = itemMapper.toItem(itemDto);
        item.setOwner(owner);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId) {
        return null;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getAllUserItems(Long ownerId) {
        return List.of();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return List.of();
    }


}