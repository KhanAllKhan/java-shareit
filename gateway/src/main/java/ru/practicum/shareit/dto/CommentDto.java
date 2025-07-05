package ru.practicum.shareit.dto;


import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}

