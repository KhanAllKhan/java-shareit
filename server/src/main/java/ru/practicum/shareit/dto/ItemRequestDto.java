package ru.practicum.shareit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;


    private List<ItemResponseDto> responses;

    @JsonProperty("items")
    public List<ItemResponseDto> getItems() {
        return responses;
    }
}
