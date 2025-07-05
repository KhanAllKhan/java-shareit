package ru.practicum.shareit.booking;

import lombok.*;

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
