package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.service.ItemRequestService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    // POST /requests — добавить новый запрос вещи
    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @Valid @RequestBody ItemRequestDto requestDto) {
        return itemRequestService.createRequest(userId, requestDto);
    }

    // GET /requests — получить список своих запросов вместе с ответами на них,
    // отсортированных от более новых к более старым
    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOwnRequests(userId);
    }

    // GET /requests/all — получить список запросов, созданных другими пользователями,
    // отсортированных от более новых к более старым
    @GetMapping("/all")
    public List<ItemRequestDto> getOtherRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOtherUsersRequests(userId);
    }

    // GET /requests/{requestId} — получить данные об одном конкретном запросе с ответами
    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long requestId) {
        return itemRequestService.getRequestById(requestId, userId);
    }
}
