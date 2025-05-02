package ru.practicum.shareit.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);

    // Внутренний метод для получения полного объекта User (используется в других сервисах)
    User getUserEntity(Long userId);
}