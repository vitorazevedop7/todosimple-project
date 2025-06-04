package com.vitorazevedo.todosimple.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vitorazevedo.todosimple.models.User;
import com.vitorazevedo.todosimple.models.dto.UserCreateDTO;
import com.vitorazevedo.todosimple.models.dto.UserUpdateDTO;
import com.vitorazevedo.todosimple.models.enums.ProfileEnum;
import com.vitorazevedo.todosimple.repositories.UserRepository;
import com.vitorazevedo.todosimple.services.exceptions.DataBindingViolationException;
import com.vitorazevedo.todosimple.services.exceptions.ObjectNotFoundException;
import com.vitorazevedo.todosimple.services.exceptions.AuthorizationException;
import com.vitorazevedo.todosimple.security.UserSpringSecurity;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createEncodesPasswordAndAddsUserProfile() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("pass");

        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User created = userService.create(user);

        assertNull(created.getId());
        assertEquals("encoded", created.getPassword());
        assertTrue(created.getProfiles().contains(ProfileEnum.USER));
        verify(userRepository).save(created);
    }

    @Test
    void updateChangesPassword() {
        User existing = new User();
        existing.setId(1L);
        existing.setPassword("old");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("new")).thenReturn("encodedNew");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User update = new User();
        update.setId(1L);
        update.setPassword("new");

        User updated = userService.update(update);
        assertEquals("encodedNew", updated.getPassword());
        verify(userRepository).save(updated);
    }

    @Test
    void deleteHandlesRepositoryException() {
        User user = new User();
        user.setId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("constraint"))
            .when(userRepository).deleteById(2L);

        assertThrows(DataBindingViolationException.class, () -> userService.delete(2L));
    }

    @Test
    void fromDTOCreatesUser() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername("john");
        dto.setPassword("secret");

        User user = userService.fromDTO(dto);
        assertEquals("john", user.getUsername());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void fromDTOUpdatesUser() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setId(5L);
        dto.setPassword("secret");

        User user = userService.fromDTO(dto);
        assertEquals(5L, user.getId());
        assertEquals("secret", user.getPassword());
    }

    @Test
    void findByIdWithoutAuthorizationThrowsException() {
        Set<ProfileEnum> profiles = new HashSet<>();
        profiles.add(ProfileEnum.USER);
        UserSpringSecurity userSS = new UserSpringSecurity(2L, "jane", "pass", profiles);

        try (MockedStatic<UserService> mocked = mockStatic(UserService.class)) {
            mocked.when(UserService::authenticated).thenReturn(userSS);
            assertThrows(AuthorizationException.class, () -> userService.findById(1L));
        }
    }

    @Test
    void findByIdWhenUserNotFoundThrowsException() {
        Set<ProfileEnum> profiles = new HashSet<>();
        profiles.add(ProfileEnum.USER);
        UserSpringSecurity userSS = new UserSpringSecurity(1L, "john", "pass", profiles);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try (MockedStatic<UserService> mocked = mockStatic(UserService.class)) {
            mocked.when(UserService::authenticated).thenReturn(userSS);
            assertThrows(ObjectNotFoundException.class, () -> userService.findById(1L));
        }
    }
}
