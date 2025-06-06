package ru.practicum.shareit.service;

import java.util.List;

import ru.practicum.shareit.user.dto.UserDto;


public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);
}
