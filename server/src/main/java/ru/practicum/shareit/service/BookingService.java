package ru.practicum.shareit.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingDto bookingDto);

    BookingDto updateBookingStatus(Long bookingId, Long userId, boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookingsForUser(Long userId, String state, int from, int size);

    List<BookingDto> getBookingsForOwner(Long ownerId, String state, int from, int size);
}
