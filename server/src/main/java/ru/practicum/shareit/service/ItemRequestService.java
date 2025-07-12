package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto);

    // Теперь с параметрами from, size
    List<ItemRequestDto> getOwnRequests(Long userId, int from, int size);

    // И тоже с пагинацией
    List<ItemRequestDto> getOtherUsersRequests(Long userId, int from, int size);

    ItemRequestDto getRequestById(Long requestId, Long userId);
}
