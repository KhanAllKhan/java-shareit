package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

@Data
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;
}
