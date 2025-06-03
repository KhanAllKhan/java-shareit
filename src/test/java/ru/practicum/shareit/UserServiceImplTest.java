package ru.practicum.shareit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.service.UserServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // Дополнительные настройки не требуются
    }

    @Test
    public void testCreateUser() {
        User user = User.builder().id(1L)
                .name("John")
                .email("john@example.com")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = UserDto.builder()
                .name("John")
                .email("john@example.com")
                .build();

        UserDto created = userService.createUser(userDto);
        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("John", created.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser() {
        User existing = User.builder().id(1L)
                .name("John")
                .email("john@example.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        UserDto updateDto = UserDto.builder()
                .name("Johnny")
                .email("john@example.com")
                .build();
        UserDto updated = userService.updateUser(1L, updateDto);
        assertEquals("Johnny", updated.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    public void testGetUserById_Found() {
        User existing = User.builder().id(1L)
                .name("John")
                .email("john@example.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        UserDto dto = userService.getUserById(1L);
        assertEquals("John", dto.getName());
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getUserById(99L));
    }

    @Test
    public void testGetAllUsers() {
        User user1 = User.builder().id(1L)
                .name("John")
                .email("john@example.com")
                .build();
        User user2 = User.builder().id(2L)
                .name("Jane")
                .email("jane@example.com")
                .build();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        List<UserDto> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testDeleteUser() {
        // Для удаления просто проверяем, что метод deleteById вызывается
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
