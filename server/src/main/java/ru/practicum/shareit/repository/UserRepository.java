package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
