package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class InMemoryUserService implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(idCounter.getAndIncrement());
        users.put(user.getId(), user);
        log.info("Создан новый пользователь: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User existingUser = users.get(userId);
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        log.info("Обновлен пользователь с ID {}: {}", userId, existingUser);
        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return new ArrayList<>(users.values()).stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public void deleteUser(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        users.remove(userId);
        log.info("Удален пользователь с ID: {}", userId);
    }

    @Override
    public User getUserEntity(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        return user;
    }
}