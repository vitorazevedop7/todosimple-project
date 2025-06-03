package com.vitorazevedo.todosimple.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vitorazevedo.todosimple.models.User;
import com.vitorazevedo.todosimple.models.enums.ProfileEnum;
import com.vitorazevedo.todosimple.repositories.UserRepository;
import com.vitorazevedo.todosimple.security.UserSpringSecurity;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void whenUserExistsLoadByUsernameReturnsUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setPassword("pass");
        Set<Integer> profiles = new HashSet<>();
        profiles.add(ProfileEnum.USER.getCode());
        user.setProfiles(profiles);

        when(userRepository.findByUsername("john")).thenReturn(user);

        UserDetails details = userDetailsService.loadUserByUsername("john");
        assertNotNull(details);
        assertEquals("john", details.getUsername());
        assertEquals("pass", details.getPassword());
        assertEquals(1L, ((UserSpringSecurity) details).getId());
    }

    @Test
    void whenUserDoesNotExistThrowsException() {
        when(userRepository.findByUsername("missing")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("missing");
        });
    }
}
