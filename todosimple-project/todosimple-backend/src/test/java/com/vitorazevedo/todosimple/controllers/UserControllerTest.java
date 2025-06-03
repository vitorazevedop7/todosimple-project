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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.vitorazevedo.todosimple.models.User;
import com.vitorazevedo.todosimple.models.enums.ProfileEnum;
import com.vitorazevedo.todosimple.security.UserSpringSecurity;
import com.vitorazevedo.todosimple.services.UserService;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
}
