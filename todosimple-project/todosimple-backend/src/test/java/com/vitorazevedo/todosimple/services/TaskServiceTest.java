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

import com.vitorazevedo.todosimple.models.Task;
import com.vitorazevedo.todosimple.models.User;
import com.vitorazevedo.todosimple.models.enums.ProfileEnum;
import com.vitorazevedo.todosimple.repositories.TaskRepository;
import com.vitorazevedo.todosimple.security.UserSpringSecurity;
import com.vitorazevedo.todosimple.services.exceptions.ObjectNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void findByIdWithOwnerUserReturnsTask() {
        User owner = new User();
        owner.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setUser(owner);
        task.setDescription("Test task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Set<ProfileEnum> profiles = new HashSet<>();
        profiles.add(ProfileEnum.USER);
        UserSpringSecurity userSS = new UserSpringSecurity(1L, "john", "pass", profiles);

        try (MockedStatic<UserService> mocked = mockStatic(UserService.class)) {
            mocked.when(UserService::authenticated).thenReturn(userSS);

            Task result = taskService.findById(1L);
            assertEquals(task, result);
        }
    }

    @Test
    void findByIdWithDifferentUserThrowsException() {
        User owner = new User();
        owner.setId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setUser(owner);
        task.setDescription("Test task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Set<ProfileEnum> profiles = new HashSet<>();
        profiles.add(ProfileEnum.USER);
        UserSpringSecurity userSS = new UserSpringSecurity(2L, "jane", "pass", profiles);

        try (MockedStatic<UserService> mocked = mockStatic(UserService.class)) {
            mocked.when(UserService::authenticated).thenReturn(userSS);

            assertThrows(ObjectNotFoundException.class, () -> taskService.findById(1L));
        }
    }
}
