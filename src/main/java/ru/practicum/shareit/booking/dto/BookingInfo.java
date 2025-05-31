package ru.practicum.shareit.booking.dto;

import lombok.*;
// для отправки
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfo {
    private Long id;
    private Long bookerId;
}
