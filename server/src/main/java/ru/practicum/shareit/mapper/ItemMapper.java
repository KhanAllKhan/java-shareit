package ru.practicum.shareit.mapper;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.dto.ItemDto;

import java.util.Collections;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        if (item == null) return null;
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner() != null ? UserMapper.toUserDto(item.getOwner()) : null)
                .requestId(null)
                .comments(item.getComments() != null
                        ? item.getComments().stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .lastBooking(null)
                .nextBooking(null)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        if (itemDto == null) return null;
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
