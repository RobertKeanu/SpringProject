package org.store.springproject.test.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.store.springproject.exception.UserAlreadyExistsException;
import org.store.springproject.model.User;
import org.store.springproject.repository.UserRepository;
import org.store.springproject.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUser_Success() {
        String username = "testuser12";
        String rawPassword = "password123!";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = userService.createUser(username, rawPassword);

        assertNotNull(createdUser);
        assertEquals(username, createdUser.getUsername());
        assertFalse(passwordEncoder.matches(rawPassword, createdUser.getPassword()));

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_AlreadyExists() {
        String username = "testuser12";
        String password = "password123";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User(username, "existing_password_hash")));

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(username, password);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsername_Success() {
        String username = "testuser12";
        User user = new User(username, "someHash");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void findByUsername_NotFound() {
        String username = "unknown_user";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByUsername(username);
    }
}
