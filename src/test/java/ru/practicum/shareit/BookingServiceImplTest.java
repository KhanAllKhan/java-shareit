package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.Status.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.BookingServiceImpl;
import ru.practicum.shareit.user.User;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User booker;
    private User owner;
    private Item item;
    private Booking booking;

    @BeforeEach
    public void setUp() {
        owner = User.builder().id(2L).name("Owner").email("owner@example.com").build();
        booker = User.builder().id(1L).name("Booker").email("booker@example.com").build();
        item = Item.builder().id(10L)
                .name("Item")
                .description("Desc")
                .available(true)
                .owner(owner)
                .build();
        booking = Booking.builder()
                .id(100L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
    }

    @Test
    public void testCreateBookingSuccess() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(10L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.createBooking(1L, bookingDto);
        assertEquals(100L, result.getId());
        assertEquals("WAITING", result.getStatus());

        verify(userRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).findById(10L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testCreateBookingItemNotFound() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(10L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    public void testGetBookingByIdSuccess() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        BookingDto dto = bookingService.getBookingById(100L, 1L);
        assertEquals(100L, dto.getId());
    }

    @Test
    public void testGetBookingByIdNotAllowed() {
        // Если вызывающий не является владельцем или бронирующим, должно выброситься исключение.
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        assertThrows(IllegalArgumentException.class, () -> bookingService.getBookingById(100L, 999L));
    }
}
