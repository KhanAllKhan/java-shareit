package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.CommentRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.service.ItemServiceImpl;
import ru.practicum.shareit.user.User;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private Item item;

    @BeforeEach
    public void setUp() {
        owner = User.builder().id(10L)
                .name("Owner")
                .email("owner@example.com")
                .build();
        item = Item.builder().id(100L)
                .name("Item1")
                .description("Desc")
                .available(true)
                .owner(owner)
                .build();
    }

    @Test
    public void testCreateItemSuccess() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto dto = ItemDto.builder()
                .name("Item1")
                .description("Desc")
                .available(true)
                .build();
        ItemDto result = itemService.createItem(dto, 10L);
        assertEquals(100L, result.getId());
        verify(userRepository, times(1)).findById(10L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testUpdateItemSuccess() {
        when(itemRepository.findById(100L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        // Обновление названия вещи
        ItemDto updateDto = ItemDto.builder()
                .name("ItemUpdated")
                .build();
        ItemDto result = itemService.updateItem(100L, updateDto, 10L);
        assertEquals("ItemUpdated", result.getName());
    }

    @Test
    public void testGetItemByIdNotFound() {
        when(itemRepository.findById(200L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> itemService.getItemById(200L));
    }

    @Test
    public void testSearchItemsEmpty() {
        List<Item> emptyList = new ArrayList<>();
        when(itemRepository.search("Test")).thenReturn(emptyList);
        List<ItemDto> result = itemService.searchItems("Test");
        assertTrue(result.isEmpty());
    }
}
