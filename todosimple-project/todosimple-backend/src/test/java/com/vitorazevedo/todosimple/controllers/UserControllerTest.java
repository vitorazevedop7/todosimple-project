package com.vitorazevedo.todosimple.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorazevedo.todosimple.models.User;
import com.vitorazevedo.todosimple.models.dto.UserCreateDTO;
import com.vitorazevedo.todosimple.models.dto.UserUpdateDTO;
import com.vitorazevedo.todosimple.models.enums.ProfileEnum;
import com.vitorazevedo.todosimple.security.UserSpringSecurity;
import com.vitorazevedo.todosimple.services.UserService;
import com.vitorazevedo.todosimple.configs.TestSecurityConfig;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void getLoggedUserReturnsUser() throws Exception {
        Set<ProfileEnum> profiles = new HashSet<>();
        profiles.add(ProfileEnum.USER);
        UserSpringSecurity userSS = new UserSpringSecurity(1L, "john", "pass", profiles);
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(userService.findById(1L)).thenReturn(user);

        try (MockedStatic<UserService> mocked = mockStatic(UserService.class)) {
            mocked.when(UserService::authenticated).thenReturn(userSS);

            mockMvc.perform(get("/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john"));
        }
    }

    @Test
    void getUserByIdReturnsUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void createUserReturnsLocation() throws Exception {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("john");
        dto.setPassword("secret123");
        User user = new User();
        user.setUsername("john");
        user.setPassword("secret123");
        when(userService.fromDTO(any(UserCreateDTO.class))).thenReturn(user);
        when(userService.create(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(2L);
            return u;
        });

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/user/2"));
    }

    @Test
    void updateUserReturnsNoContent() throws Exception {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setPassword("newpassword");
        User user = new User();
        user.setId(1L);
        user.setPassword("newpassword");
        when(userService.fromDTO(any(UserUpdateDTO.class))).thenReturn(user);
        when(userService.update(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserReturnsNoContent() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/user/1"))
            .andExpect(status().isNoContent());
    }
}
