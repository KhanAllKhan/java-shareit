package ru.practicum.shareit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;


public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    // Возвращает запросы текущего пользователя, отсортированные от более новых к более старым
    Page<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(Long requestorId, Pageable pageable);

    // Для метода GET /requests/all (с пагинацией)
    Page<ItemRequest> findAllByRequestor_IdNotOrderByCreatedDesc(Long requestorId, Pageable pageable);
}
