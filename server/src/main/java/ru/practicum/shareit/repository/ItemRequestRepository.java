package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    // Возвращает запросы текущего пользователя, отсортированные от более новых к более старым
    List<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(Long requestorId);

    // Возвращает запросы, созданные другими пользователями, отсортированные от новых к старым
    List<ItemRequest> findAllByRequestor_IdNotOrderByCreatedDesc(Long requestorId);
}
