package com.vitorazevedo.todosimple.controllers;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.vitorazevedo.todosimple.models.Task;
import com.vitorazevedo.todosimple.models.projection.TaskProjection;
import com.vitorazevedo.todosimple.services.TaskService;

@RestController
@RequestMapping("/task")
@Validated  
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    // CODE REVIEW [ARQUITETURA / DTO]: O controller retorna a entidade JPA Task diretamente.
    // Isso cria acoplamento entre a camada de persistência e a API REST.
    // Mudanças na entidade alteram automaticamente a resposta da API sem intenção.
    // O projeto já usa DTOs de entrada (UserCreateDTO) — aplicar o mesmo padrão na saída.
    // Sugestão: criar TaskResponseDTO com apenas id e description.
    @GetMapping("/{id}")
    @Operation(summary = "Find task by id",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {@ApiResponse(responseCode = "200", description = "Task found")})
    public ResponseEntity<Task> getTaskById(@Parameter(description = "Task id") @PathVariable Long id) {
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok(obj);
    }

    // CODE REVIEW [ESCALABILIDADE]: Endpoint sem paginação retorna todos os registros de uma vez.
    // Se um usuário tiver milhares de tarefas, toda a lista é carregada em memória e
    // transmitida ao cliente, causando degradação de performance e alto consumo de memória.
    // Sugestão: usar Pageable do Spring Data:
    //   public ResponseEntity<Page<TaskProjection>> findAllByUser(
    //       @PageableDefault(size = 20, sort = "id") Pageable pageable)
    // No service e repositório: Page<TaskProjection> findByUser_Id(Long userId, Pageable pageable);
    @GetMapping("/user")
    @Operation(summary = "List tasks of logged user",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {@ApiResponse(responseCode = "200", description = "List of tasks")})
    public ResponseEntity<List<TaskProjection>> findAllByUser() {
        List<TaskProjection> list = this.taskService.findAllByUser();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    @Validated
    @Operation(summary = "Create new task",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {@ApiResponse(responseCode = "201", description = "Task created")})
    public ResponseEntity<Void> create(@Validated @RequestBody Task obj) {
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
        }

    @PutMapping("/{id}")
    @Operation(summary = "Update task",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {@ApiResponse(responseCode = "204", description = "Task updated")})
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @Parameter(description = "Task id") @PathVariable Long id) {
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task",
        security = @SecurityRequirement(name = "bearerAuth"),
        responses = {@ApiResponse(responseCode = "204", description = "Task deleted")})
    public ResponseEntity<Void> delete(@Parameter(description = "Task id") @PathVariable Long id) {
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
