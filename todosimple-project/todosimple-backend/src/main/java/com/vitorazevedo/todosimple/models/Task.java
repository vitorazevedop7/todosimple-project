package com.vitorazevedo.todosimple.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// CODE REVIEW [BOAS PRÁTICAS]: Ausência de campos de auditoria (createdAt, updatedAt).
// Sugestão: herdar de uma BaseEntity com @MappedSuperclass e Spring Data Auditing.
// CODE REVIEW [PADRÃO BUILDER]: Adicionar @Builder para facilitar criação de objetos em testes.
@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    public static final String TABLE_NAME = "task";

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "description", nullable = false, length = 255)
    @NotBlank // substitutes @NotNull e @NotEmpty only for Strings
    @Size(min = 1, max = 255)
    private String description;

    // CODE REVIEW [ARQUITETURA]: Campo de status ausente na entidade.
    // O frontend exibe checkbox de "concluído" mas o estado não é persistido no banco.
    // Ao recarregar a página, todas as tarefas voltam a aparecer como não concluídas.
    // Sugestão: adicionar campo de status para persistir o estado real:
    //   @Column(nullable = false)
    //   private boolean completed = false;
    // Ou com enum para maior flexibilidade:
    //   @Enumerated(EnumType.STRING)
    //   private TaskStatus status = TaskStatus.PENDING; // PENDING, IN_PROGRESS, DONE

}
