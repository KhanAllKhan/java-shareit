package ru.practicum.shareit.dto;


import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class UserDto {
    private Long id;

    private String name;

    private String email;
}

