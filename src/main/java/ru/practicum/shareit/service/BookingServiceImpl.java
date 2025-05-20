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
        // Получаем книгуещего (booker)
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        // Проверяем, что вещь существует
        Item item = itemRepository.findById(bookingDto.getItem().getId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        // Если владелец пытается бронировать свою вещь – выбрасываем ошибку
        if (item.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Владелец вещи не может бронировать собственную вещь");
        }
        // Устанавливаем статус и сохраняем бронирование
        Booking booking = Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
        Booking savedBooking = bookingRepository.save(booking);
        // Здесь выполните маппинг сущности в DTO
        return BookingMapper.toBookingDto(savedBooking);
    }

    @Override
    public BookingDto updateBookingStatus(Long bookingId, Long userId, boolean approved) {
        // Получаем бронирование
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        // Проверяем, что действие выполняет владелец вещи
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Подтвердить бронирование может только владелец вещи");
        }
        // Если уже подтверждено/отклонено – нельзя изменять
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
        // Проверяем, что запрашивающий бронирование либо его автор, либо владелец вещи
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Нет прав для просмотра бронирования");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsForUser(Long userId, String state, int from, int size) {
        // Реализуйте логику фильтрации по состоянию (например, текущие, прошедшие, будущие и т.д.)
        // Пример: получить бронирования, где bookerId = userId
        List<Booking> bookings = bookingRepository.findAllByBooker_Id(userId, Sort.by(Sort.Direction.DESC, "start"));
        // Маппинг в DTO
        return BookingMapper.toBookingDtoList(bookings);
    }

    @Override
    public List<BookingDto> getBookingsForOwner(Long ownerId, String state, int from, int size) {
        // Получаем все бронирования для вещей владельца
        List<Booking> bookings = bookingRepository.findAllByItem_Owner_Id(ownerId, Sort.by(Sort.Direction.DESC, "start"));
        return BookingMapper.toBookingDtoList(bookings);
    }
}
