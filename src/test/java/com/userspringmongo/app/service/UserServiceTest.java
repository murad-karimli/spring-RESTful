package com.userspringmongo.app.service;

import com.userspringmongo.app.exception.UserException;
import com.userspringmongo.app.model.User;
import com.userspringmongo.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserById() {
        User user = new User("john.doe@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "123 Main St", "123-456-7890");
        user.setId("1");

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById("1");
        assertEquals("john.doe@example.com", foundUser.getEmail());
        assertEquals("John", foundUser.getFirstName());
        assertEquals("Doe", foundUser.getLastName());
    }

    @Test
    void testFindUserByIdNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(UserException.class, () -> userService.getUserById("1"));
    }
}
