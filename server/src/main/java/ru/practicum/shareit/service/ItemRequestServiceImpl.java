package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.repository.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequest;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRepository itemRepository;
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;


    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest request = ItemRequest.builder()
                .description(requestDto.getDescription())
                .created(LocalDateTime.now())
                .requestor(user)
                .build();
        ItemRequest savedRequest = requestRepository.save(request);
        return ItemRequestMapper.toItemRequestDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        List<ItemRequest> requests = requestRepository.findAllByRequestor_IdOrderByCreatedDesc(userId);
        return requests.stream().map(req -> {
            ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(req);
            // Получаем список вещей, созданных в ответ на этот запрос
            List<Item> items = itemRepository.findAllByRequest_Id(req.getId());
            List<ItemResponseDto> responses = items.stream()
                    .map(item -> ItemResponseDto.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .ownerId(item.getOwner().getId())
                            .build())
                    .collect(Collectors.toList());
            dto.setResponses(responses);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getOtherUsersRequests(Long userId) {
        List<ItemRequest> requests = requestRepository.findAllByRequestor_IdNotOrderByCreatedDesc(userId);
        return requests.stream().map(req -> {
            ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(req);
            List<Item> items = itemRepository.findAllByRequest_Id(req.getId());
            List<ItemResponseDto> responses = items.stream()
                    .map(item -> ItemResponseDto.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .ownerId(item.getOwner().getId())
                            .build())
                    .collect(Collectors.toList());
            dto.setResponses(responses);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        // Проверяем, что запрашивающий пользователь существует
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        ItemRequestDto dto = ItemRequestMapper.toItemRequestDto(request);
        List<Item> items = itemRepository.findAllByRequest_Id(request.getId());
        List<ItemResponseDto> responses = items.stream()
                .map(item -> ItemResponseDto.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .ownerId(item.getOwner().getId())
                        .build())
                .collect(Collectors.toList());
        dto.setResponses(responses);
        return dto;
    }
}
