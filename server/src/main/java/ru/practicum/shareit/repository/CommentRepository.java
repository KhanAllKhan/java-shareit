package ru.practicum.shareit.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItem_Id(Long itemId);
}
