package ru.practicum.shareit.request.dto;

import lombok.*;
import java.time.LocalDateTime;

@ToString
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}