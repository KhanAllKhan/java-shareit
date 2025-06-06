package ru.practicum.shareit.request.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemResponseDto> responses;
}