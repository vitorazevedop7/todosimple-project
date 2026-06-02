package com.vitorazevedo.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vitorazevedo.todosimple.models.Task;
import com.vitorazevedo.todosimple.models.projection.TaskProjection;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Repository pra Task pelo Spring
    List<TaskProjection> findByUser_Id(Long userId);

    // CODE REVIEW [PERFORMANCE]: Sugestão de método para verificar propriedade da tarefa
    // sem carregar o objeto completo. Usado pelo TaskService.userHasTask() via query eficiente.
    // boolean existsByIdAndUser_Id(Long taskId, Long userId);

    // Repository pra Task pelo Query
    // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    // List<Task> findByUser_Id(@Param("id") Long id);

    // Repository pra Task pelo SQL
    // @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    // List<Task> findByUser_Id(@Param("id") Long userId);

}
