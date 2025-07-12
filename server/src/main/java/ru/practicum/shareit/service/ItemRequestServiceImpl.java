package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.dto.ItemResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.ItemRequestMapper;
import ru.practicum.shareit.repository.ItemRequestRepository;
import ru.practicum.shareit.repository.ItemRepository;
import ru.practicum.shareit.repository.UserRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id=" + userId));

        ItemRequest request = ItemRequest.builder()
                .description(requestDto.getDescription())
                .created(LocalDateTime.now())
                .requestor(user)
                .build();

        var saved = requestRepository.save(request);
        return ItemRequestMapper.toItemRequestDto(saved);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден id=" + userId);
        }
        Pageable page = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> requests = requestRepository
                .findAllByRequestor_IdOrderByCreatedDesc(userId, page)
                .getContent();

        return mapToDtoWithResponses(requests);
    }

    @Override
    public List<ItemRequestDto> getOtherUsersRequests(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден id=" + userId);
        }
        Pageable page = PageRequest.of(from / size, size, Sort.by("created").descending());
        List<ItemRequest> requests = requestRepository
                .findAllByRequestor_IdNotOrderByCreatedDesc(userId, page)
                .getContent();

        return mapToDtoWithResponses(requests);
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден id=" + userId);
        }
        var request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден id=" + requestId));

        var dto = ItemRequestMapper.toItemRequestDto(request);
        dto.setResponses(
                itemRepository.findAllByRequest_Id(request.getId())
                        .stream()
                        .map(item -> ItemResponseDto.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .ownerId(item.getOwner().getId())
                                .build())
                        .collect(Collectors.toList())
        );
        return dto;
    }

    // Вспомогательный метод для маппинга списка запросов в DTO с ответами
    private List<ItemRequestDto> mapToDtoWithResponses(List<ItemRequest> requests) {
        return requests.stream()
                .map(req -> {
                    var dto = ItemRequestMapper.toItemRequestDto(req);
                    dto.setResponses(
                            itemRepository.findAllByRequest_Id(req.getId())
                                    .stream()
                                    .map(item -> ItemResponseDto.builder()
                                            .id(item.getId())
                                            .name(item.getName())
                                            .ownerId(item.getOwner().getId())
                                            .build())
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
