package com.vitorazevedo.todosimple.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class JWTUtilTest {

    @Test
    void generateTokenAndValidate() {
        JWTUtil jwtUtil = new JWTUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "01234567890123456789012345678912");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 60000L);

        String token = jwtUtil.generateToken("john");
        assertNotNull(token);
        assertTrue(jwtUtil.isValidToken(token));
        assertEquals("john", jwtUtil.getUsername(token));
    }

    @Test
    void invalidTokenReturnsFalse() {
        JWTUtil jwtUtil = new JWTUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "01234567890123456789012345678912");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 60000L);

        assertFalse(jwtUtil.isValidToken("invalid"));
        assertNull(jwtUtil.getUsername("invalid"));
    }
}
