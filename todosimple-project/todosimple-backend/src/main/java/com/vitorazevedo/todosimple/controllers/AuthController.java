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
