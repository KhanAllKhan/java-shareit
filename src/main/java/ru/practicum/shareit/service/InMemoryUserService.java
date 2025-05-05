package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class InMemoryUserService implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 1;

    @Override
    public UserDto createUser(UserDto userDto) {
        checkEmailUniqueness(userDto.getEmail(), null);

        User user = UserMapper.toUser(userDto);
        user.setId(idCounter++);
        users.put(user.getId(), user);

        log.info("Created user: ID={}, Email={}", user.getId(), user.getEmail());
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = getUserOrThrow(userId);

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            checkEmailUniqueness(userDto.getEmail(), userId);
            existingUser.setEmail(userDto.getEmail());
        }

        log.info("Updated user: ID={}", userId);
        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(getUserOrThrow(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return new ArrayList<>(users.values()).stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.remove(userId) == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        log.info("Deleted user: ID={}", userId);
    }

    @Override
    public User getUserEntity(Long userId) {
        return getUserOrThrow(userId);
    }

    private User getUserOrThrow(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }

    private void checkEmailUniqueness(String email, Long excludeUserId) {
        if (email == null) return;

        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email) &&
                    (excludeUserId == null || !user.getId().equals(excludeUserId))) {
                throw new DuplicateEmailException("Email '" + email + "' already in use");
            }
        }
    }
}