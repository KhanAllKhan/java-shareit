package ru.practicum.shareit.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponseDto> responses;
}
