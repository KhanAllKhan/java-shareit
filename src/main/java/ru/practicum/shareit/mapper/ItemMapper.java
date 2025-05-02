package ru.practicum.shareit.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.service.UserService;
import ru.practicum.shareit.user.User;

@Component
public class ItemMapper {
    private final UserService userService;

    public ItemMapper(UserService userService) {
        this.userService = userService;
    }

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .owner(item.getOwner() != null ? ru.practicum.shareit.mapper.UserMapper.toUserDto(item.getOwner()) : null)
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public Item toItem(ItemDto itemDto) {
        User owner = null;
        if (itemDto.getOwner() != null) {
            owner = userService.getUserEntity(itemDto.getOwner().getId());
        }

        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)  // Теперь передаем User, а не UserDto
                .build();
    }
}