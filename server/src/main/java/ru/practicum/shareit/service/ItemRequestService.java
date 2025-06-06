package ru.practicum.shareit.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getOtherUsersRequests(Long userId);

    ItemRequestDto getRequestById(Long requestId, Long userId);
}
