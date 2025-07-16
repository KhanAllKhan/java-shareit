package ru.practicum.shareit.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.dto.BookingDto;
import ru.practicum.shareit.status.Status;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    /**
     * Преобразует сущность Booking в объект DTO.
     */
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus().name())
                .build();
    }

    /**
     * Преобразует DTO бронирования в сущность Booking.
     * Обратите внимание – для полей item и booker обычно требуется отдельно,
     * например, получить из базы данных по id, поэтому этот маппинг может быть не полным.
     */
    public static Booking toBooking(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(Status.valueOf(bookingDto.getStatus()))
                .build();
    }

    /**
     * Преобразует список сущностей Booking в список объектов DTO.
     */
    public static List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        if (bookings == null) {
            return Collections.emptyList();
        }
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
