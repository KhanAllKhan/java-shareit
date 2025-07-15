package ru.practicum.shareit.dto;


import lombok.*;


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

    private String name;


    private String description;


    @Getter
    private Boolean available;

    private UserDto owner;
    private Long requestId;

    private BookingInfo lastBooking;
    private BookingInfo nextBooking;
    private List<CommentDto> comments;
}
