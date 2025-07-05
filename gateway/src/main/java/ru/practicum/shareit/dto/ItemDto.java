package ru.practicum.shareit.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.shareit.booking.BookingInfo;


import java.util.List;

@ToString
@Getter
@Setter
@EqualsAndHashCode
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
    private List<CommentDto> comments;
}
