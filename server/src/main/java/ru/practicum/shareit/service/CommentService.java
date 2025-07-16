package ru.practicum.shareit.service;


import ru.practicum.shareit.dto.CommentDto;

public interface CommentService {
    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);
}
