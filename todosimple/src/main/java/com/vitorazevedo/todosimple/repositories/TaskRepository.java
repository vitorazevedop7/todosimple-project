package com.vitorazevedo.todosimple.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vitorazevedo.todosimple.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
   
    // Repository pra Task pelo Spring 
    List<Task> findByUser_Id(Long userId);

    // Repository pra Task pelo Query
    // @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    // List<Task> findByUser_Id(@Param("id") Long id);

    // Repository pra Task pelo SQL
    // @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    // List<Task> findByUser_Id(@Param("id") Long userId);

}
