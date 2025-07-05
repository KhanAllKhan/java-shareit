package ru.practicum.shareit.client;


import ru.practicum.shareit.dto.CommentDto;
import ru.practicum.shareit.dto.ItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;




@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long ownerId, ItemDto itemDto) {
        return post("", ownerId, itemDto);
    }

    public ResponseEntity<Object> updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        return patch("/" + itemId, ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllUserItems(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/ru/practicum/comment", userId, commentDto);
    }
}
