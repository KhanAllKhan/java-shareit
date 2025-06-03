package ru.practicum.shareit.comment.dto;


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

