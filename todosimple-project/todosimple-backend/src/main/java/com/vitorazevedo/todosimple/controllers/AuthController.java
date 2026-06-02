package com.vitorazevedo.todosimple.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vitorazevedo.todosimple.models.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class AuthController {

    // CODE REVIEW [ARQUITETURA]: Este método nunca é executado em produção.
    // O endpoint POST /login é interceptado pelo JWTAuthenticatorFilter antes de chegar
    // ao controller. Um desenvolvedor novo lendo o código buscará a lógica de login aqui
    // e não a encontrará, gerando confusão arquitetural.
    // Sugestão: remover este controller e documentar diretamente no JWTAuthenticatorFilter
    // ou no SecurityConfig que o login é gerenciado pelo filtro.
    // Caso queira manter apenas para fins de documentação Swagger, adicionar comentário explícito.
    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Returns a JWT token in the Authorization header on success",
        requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = User.class))),
        responses = {
            @ApiResponse(responseCode = "200", description = "Authenticated")
        }
    )
    public ResponseEntity<Void> login() {
        return ResponseEntity.ok().build();
    }
}
