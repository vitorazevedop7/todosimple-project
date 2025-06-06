package com.vitorazevedo.todosimple.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {
    
    private Long id;

    @NotBlank
    @Size(min = 4, max = 60, message = "Password must contain at least 4 characters")
    private String password;

}
