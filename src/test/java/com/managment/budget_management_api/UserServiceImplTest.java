package com.managment.budget_management_api;

import com.managment.budget_management_api.Exceptions.UserNotFoundException;
import com.managment.budget_management_api.Model.User;
import com.managment.budget_management_api.Repository.UserRepository;
import com.managment.budget_management_api.Service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUsername("TestUser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setRole("USER");
    }

    @Test
    void shouldSaveUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(testUser);

        assertNotNull(savedUser);
        assertEquals("TestUser", savedUser.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldThrowExceptionWhenSavingNullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(null);
        });

        assertEquals("User cannot be null", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateExistingUserSuccessfully() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = new User();
        updatedUser.setUsername("UpdatedUser");

        User result = userService.update(1, updatedUser);

        assertEquals("UpdatedUser", result.getUsername());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setUsername("UpdatedUser");

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.update(1, updatedUser);
        });

        assertEquals("User with ID: 1 not found", exception.getMessage());
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userService.findById(1);

        assertTrue(foundUser.isPresent());
        assertEquals("TestUser", foundUser.get().getUsername());
    }

    @Test
    void shouldReturnEmptyOptionalWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findById(1);

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        assertDoesNotThrow(() -> userService.deleteById(1));

        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteById(1);
        });

        assertEquals("User with ID: 1 not found", exception.getMessage());
        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldAddAdminUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User adminUser = new User();
        adminUser.setUserId(1);
        adminUser.setUsername("AdminUser");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("adminPass");

        User savedAdmin = userService.addAdminUser(adminUser);

        assertEquals("ADMIN", savedAdmin.getRole());
        verify(userRepository, times(1)).save(adminUser);
    }

}
