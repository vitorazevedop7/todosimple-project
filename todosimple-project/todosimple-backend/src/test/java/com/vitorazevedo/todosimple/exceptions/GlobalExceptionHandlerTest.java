package com.vitorazevedo.todosimple.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.vitorazevedo.todosimple.services.exceptions.AuthorizationException;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        // avoid stack trace in assertions
        ReflectionTestUtils.setField(handler, "printStackTrace", false);
    }

    @Test
    void handleDataIntegrityViolationReturnsConflict() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("constraint", new RuntimeException("unique"));

        ResponseEntity<Object> response = handler.handleDataIntegrityViolationException(ex, null);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.CONFLICT.value(), body.getStatus());
        assertEquals("unique", body.getMessage());
    }

    @Test
    void handleConstraintViolationReturnsUnprocessableEntity() {
        ConstraintViolationException ex = new ConstraintViolationException("invalid", null);

        ResponseEntity<Object> response = handler.handleConstraintViolationException(ex, null);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), body.getStatus());
        assertEquals("invalid", body.getMessage());
    }

    @Test
    void handleAuthorizationExceptionReturnsForbidden() {
        AuthorizationException ex = new AuthorizationException("denied");

        ResponseEntity<Object> response = handler.handleAuthorizationException(ex, null);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse body = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.FORBIDDEN.value(), body.getStatus());
        assertEquals("denied", body.getMessage());
    }
}
