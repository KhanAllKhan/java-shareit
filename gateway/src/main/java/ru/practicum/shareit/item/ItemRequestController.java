
package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.ItemRequestDto;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Create item request by user {}", userId);
        return itemRequestClient.createItemRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all user {} item requests", userId);
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get all item requests without user {}", userId);
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item request {}", requestId);
        return itemRequestClient.getItemRequest(requestId, userId);
    }
}