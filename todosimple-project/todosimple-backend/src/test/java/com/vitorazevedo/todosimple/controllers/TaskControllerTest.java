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
import com.vitorazevedo.todosimple.models.projection.TaskProjection;
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

    @Test
    void getTaskByIdReturnsTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("task 1");
        when(taskService.findById(1L)).thenReturn(task);

        mockMvc.perform(get("/task/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.description").value("task 1"));
    }

    @Test
    void getTasksByUserReturnsList() throws Exception {
        TaskProjection proj = new TaskProjection() {
            public Long getId() { return 2L; }
            public String getDescription() { return "task 2"; }
        };
        when(taskService.findAllByUser()).thenReturn(java.util.List.of(proj));

        mockMvc.perform(get("/task/user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(2))
            .andExpect(jsonPath("$[0].description").value("task 2"));
    }

    @Test
    void updateTaskReturnsNoContent() throws Exception {
        Task task = new Task();
        task.setDescription("updated");
        doNothing().when(taskService).update(any(Task.class));

        mockMvc.perform(put("/task/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(task)))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteTaskReturnsNoContent() throws Exception {
        doNothing().when(taskService).delete(1L);

        mockMvc.perform(delete("/task/1"))
            .andExpect(status().isNoContent());
    }
}
