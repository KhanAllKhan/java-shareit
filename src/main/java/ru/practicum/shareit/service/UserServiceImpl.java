package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.mapper.UserMapper;
import ru.practicum.shareit.repository.UserRepository;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        // Преобразование DTO в сущность и сохранение
        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        // Преобразование сохраненной сущности в DTO
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        // Обновляем имя, если оно задано
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        // Обновляем email, если он задан
        if (userDto.getEmail() != null) {
            // Если email изменился (новый email не совпадает с текущим)
            if (!userDto.getEmail().equals(user.getEmail())) {
                // Проверяем, не используется ли новый email другим пользователем
                Optional<User> userByEmail = userRepository.findByEmail(userDto.getEmail());
                if (userByEmail.isPresent() && !userByEmail.get().getId().equals(userId)) {
                    throw new DuplicateEmailException("Email " + userDto.getEmail() + " уже используется.");
                }
                user.setEmail(userDto.getEmail());
            }
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.toUserDto(updatedUser);
    }


    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
