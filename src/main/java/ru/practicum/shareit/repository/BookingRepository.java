package ru.practicum.shareit.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Получение всех бронирований по идентификатору пользователя-заключателя (booker)
    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    // Получение бронирований, у которых дата окончания раньше заданного момента времени (например, для прошедших бронирований)
    List<Booking> findByBooker_IdAndEndBefore(Long bookerId, LocalDateTime endTime, Sort sort);

    // Получение бронирований для вещей владельца
    List<Booking> findAllByItem_Owner_Id(Long ownerId, Sort sort);

    // Получение последнего бронирования для вещи (с датой начала меньше текущего времени)
    Booking findFirstByItem_IdAndStartBefore(Long itemId, LocalDateTime time, Sort sort);

    // Получение ближайшего будущего бронирования для вещи (с датой начала больше текущего времени)
    Booking findFirstByItem_IdAndStartAfter(Long itemId, LocalDateTime time, Sort sort);

    List<Booking> findByBooker_IdAndItem_IdAndEndBefore(Long bookerId, Long itemId, LocalDateTime end, Sort sort);

}
