package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingInfo;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.CommentMapper;
import ru.practicum.shareit.mapper.ItemMapper;
import ru.practicum.shareit.repository.BookingRepository;
import ru.practicum.shareit.repository.CommentRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!item.getOwner().getId().equals(ownerId)) {
            return ItemMapper.toItemDto(item); // Возвращаем текущий объект без изменений
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        Item updatedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllUserItems(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwner_Id(ownerId);
        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toItemDto(item);
                    BookingInfo lastBooking = getLastBookingInfo(item.getId());
                    BookingInfo nextBooking = getNextBookingInfo(item.getId());
                    itemDto.setLastBooking(lastBooking);
                    itemDto.setNextBooking(nextBooking);
                    return itemDto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository.search(text);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }


    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        List<Booking> pastBookings = bookingRepository.findByBooker_IdAndItem_IdAndEndBefore(
                userId, itemId, LocalDateTime.now(), Sort.by(Sort.Direction.ASC, "end")
        );
        if (pastBookings.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не брал эту вещь в аренду или срок аренды не завершён");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(savedComment);
    }



    private BookingInfo getLastBookingInfo(Long itemId) {
        Booking booking = bookingRepository.findFirstByItem_IdAndStartBefore(
                itemId, LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start")
        );
        return booking != null ? new BookingInfo(booking.getId(), booking.getBooker().getId()) : null;
    }

    private BookingInfo getNextBookingInfo(Long itemId) {
        Booking booking = bookingRepository.findFirstByItem_IdAndStartAfter(
                itemId, LocalDateTime.now(), Sort.by(Sort.Direction.ASC, "start")
        );
        return booking != null ? new BookingInfo(booking.getId(), booking.getBooker().getId()) : null;
    }
}
