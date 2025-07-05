package ru.practicum.shareit.service;


import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllUserItems(Long ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);
}
