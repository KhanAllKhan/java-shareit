package ru.practicum.shareit.request.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemResponseDto {
    private Long id;
    private String name;
    private Long ownerId;
}
