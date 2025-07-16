package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.ItemRequestDto;
import ru.practicum.shareit.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody ItemRequestDto requestDto) {
        return requestService.createRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
             @RequestParam(defaultValue = "0") int from,
             @RequestParam(defaultValue = "10") int size) {

        return requestService.getOwnRequests(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherUsersRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int from,
           @RequestParam(defaultValue = "10") int size) {

        return requestService.getOtherUsersRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        return requestService.getRequestById(requestId, userId);
    }
}
