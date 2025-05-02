package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.user.User;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}