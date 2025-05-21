package ru.practicum.shareit.item.dto;

import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull(message = "Статус доступности обязателен")
    @Getter
    private Boolean available;

    private UserDto owner;
    private Long requestId;

    private BookingInfo lastBooking;
    private BookingInfo nextBooking;
    @OneToMany(mappedBy = "item")
    private List<CommentDto> comments;
}
