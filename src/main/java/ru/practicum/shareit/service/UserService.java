package ru.practicum.shareit.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);


    User getUserEntity(Long userId);

}