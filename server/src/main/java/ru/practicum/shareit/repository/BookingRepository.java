package ru.practicum.shareit.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findByBooker_IdAndEndBefore(Long bookerId, LocalDateTime endTime, Sort sort);

    List<Booking> findAllByItem_Owner_Id(Long ownerId, Sort sort);

    Booking findFirstByItem_IdAndStartBefore(Long itemId, LocalDateTime time, Sort sort);

    Booking findFirstByItem_IdAndStartAfter(Long itemId, LocalDateTime time, Sort sort);

    List<Booking> findByBooker_IdAndItem_IdAndEndBefore(Long bookerId, Long itemId, LocalDateTime end, Sort sort);

}
