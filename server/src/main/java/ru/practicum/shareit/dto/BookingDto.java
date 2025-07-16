package ru.practicum.shareit.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private Long itemId;
    private UserDto booker;
    private String status;
}
