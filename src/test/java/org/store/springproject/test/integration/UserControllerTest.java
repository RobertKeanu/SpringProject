package org.store.springproject.test.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.store.springproject.controller.UserController;
import org.store.springproject.dto.UserDto;
import org.store.springproject.exception.UserAlreadyExistsException;
import org.store.springproject.exception.UserNotFoundException;
import org.store.springproject.model.User;
import org.store.springproject.service.UserService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserController userController = new UserController(userService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .build();
    }

    @Test
    void createAccount_Success() throws Exception {
        String username = "john_doe";
        String password = "password123";
        UserDto userDto = new UserDto(username, password);

        doReturn(new User(username, password)).when(userService).createUser(username, password);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"username\": \"john_doe\", \"password\": \"password123\" }"))
                .andExpect(status().isCreated());

        verify(userService, times(1)).createUser(username, password);
    }

    @Test
    void createAccount_UserAlreadyExists() throws Exception {
        String username = "john_doe";
        String password = "password123";

        doThrow(new UserAlreadyExistsException("User already exists"))
                .when(userService).createUser(username, password);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john_doe\",\"password\":\"password123\"}"))
                .andExpect(status().isConflict());

        verify(userService, times(1)).createUser(username, password);
    }

    @Test
    void getUserByUsername_Success() throws Exception {
        String username = "john_doe";
        User user = new User(username, "encodedPass");
        when(userService.findByUsername(username)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/john_doe/get-user-by-name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));

        verify(userService, times(1)).findByUsername(username);
    }

    @Test
    void getUserByUsername_NotFound() throws UserNotFoundException {
        String username = "unknown_user";
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        try {
            mockMvc.perform(get("/unknown_user/get-user-by-name"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(userService, times(1)).findByUsername(username);
    }
}
