package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Status.Status;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.BookingMapper;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        // Проверяем, что bookingDto содержит itemId
        if (bookingDto.getItemId() == null) {
            throw new IllegalArgumentException("В bookingDto отсутствует информация о предмете бронирования");
        }

        // Получаем пользователя (booker)
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        // Находим вещь по itemId
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        // Проверяем, что владелец не бронирует свою вещь
        if (item.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Владелец вещи не может бронировать собственную вещь");
        }

        // **Новая проверка: если вещь недоступна для бронирования, выбрасываем исключение**
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Вещь не доступна для бронирования");
        }

        // Создаём объект Booking
        Booking booking = Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(savedBooking);
    }





    @Override
    public BookingDto updateBookingStatus(Long bookingId, Long userId, boolean approved) {
        // Получаем бронирование
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Подтвердить бронирование может только владелец вещи");
        }
        if (booking.getStatus() != Status.WAITING) {
            throw new IllegalArgumentException("Невозможно изменить статус этого бронирования");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Нет прав для просмотра бронирования");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsForUser(Long userId, String state, int from, int size) {

        List<Booking> bookings = bookingRepository.findAllByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "start"));

        return BookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public List<BookingDto> getBookingsForOwner(Long ownerId, String state, int from, int size) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings = bookingRepository.findAllByItem_Owner_Id(ownerId,
                Sort.by(Sort.Direction.DESC, "start"));
        return BookingMapper.toBookingDtoList(bookings);
    }

}
