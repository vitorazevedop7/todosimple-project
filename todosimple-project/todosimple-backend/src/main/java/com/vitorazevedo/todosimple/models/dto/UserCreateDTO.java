package com.vitorazevedo.todosimple.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Data required to create a new user")
public class UserCreateDTO {
    
    @NotBlank
    @Size(min = 2, max = 100)
    @Schema(example = "johndoe")
    private String username;

    @NotBlank
    @Size(min = 8, max = 60)
    @Schema(example = "mySecret123")
    private String password;

}
