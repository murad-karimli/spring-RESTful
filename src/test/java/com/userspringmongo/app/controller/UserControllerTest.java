package com.userspringmongo.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.userspringmongo.app.exception.GlobalExceptionHandler;
import com.userspringmongo.app.model.User;
import com.userspringmongo.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId("1");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setBirthDate(LocalDate.of(1990, 1, 1));
        user1.setAddress("123 Main St");
        user1.setPhoneNumber("123-456-7890");

        User user2 = new User();
        user2.setId("2");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("jane.doe@example.com");
        user2.setBirthDate(LocalDate.of(1992, 2, 2));
        user2.setAddress("456 Elm St");
        user2.setPhoneNumber("987-654-3210");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("123-456-7890");

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setBirthDate(LocalDate.of(1992, 2, 2));
        updatedUser.setAddress("456 Elm St");
        updatedUser.setPhoneNumber("987-654-3210");

        when(userRepository.existsById(anyString())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    public void testPatchUser() throws Exception {
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
        existingUser.setAddress("123 Main St");
        existingUser.setPhoneNumber("123-456-7890");

        User patchData = new User();
        patchData.setFirstName("Jane");

        when(userRepository.findById(anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userRepository.existsById(anyString())).thenReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testSearchUsersByBirthDateRange() throws Exception {
        User user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhoneNumber("123-456-7890");

        when(userRepository.findByBirthDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(Arrays.asList(user));

        mockMvc.perform(get("/api/users/search")
                        .param("from", "1989-01-01")
                        .param("to", "1991-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        User updatedUser = new User();
        updatedUser.setId("1");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        updatedUser.setEmail("jane.doe@example.com");
        updatedUser.setBirthDate(LocalDate.of(1992, 2, 2));
        updatedUser.setAddress("456 Elm St");
        updatedUser.setPhoneNumber("987-654-3210");

        when(userRepository.existsById(anyString())).thenReturn(false);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(userRepository.existsById(anyString())).thenReturn(false);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchUsersByBirthDateRangeBadRequest() throws Exception {
        mockMvc.perform(get("/api/users/search")
                        .param("from", "2023-01-01")
                        .param("to", "2022-01-01"))
                .andExpect(status().isBadRequest());
    }
}
