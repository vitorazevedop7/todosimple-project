package com.vitorazevedo.todosimple.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorazevedo.todosimple.models.Task;
import com.vitorazevedo.todosimple.services.TaskService;

@WebMvcTest(TaskController.class)
@ExtendWith(SpringExtension.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void createReturnsLocation() throws Exception {
        Task task = new Task();
        task.setDescription("test");
        task.setId(4L);
        when(taskService.create(any(Task.class))).thenAnswer(i -> {
            Task t = i.getArgument(0);
            t.setId(4L);
            return t;
        });

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(task)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "http://localhost/task/4"));
    }
}
