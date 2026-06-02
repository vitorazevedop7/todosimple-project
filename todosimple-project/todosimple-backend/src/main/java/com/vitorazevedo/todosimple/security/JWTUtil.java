package com.vitorazevedo.todosimple.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

@Component
public class JWTUtil {
    
    @Value("${jwt.secret}")
    private String secret;

    
    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        SecretKey key = getKeyBySecret();
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + this.expiration))
            .signWith(key)
            .compact();
    }

   private SecretKey getKeyBySecret() {
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());
        return key;
   }

   public boolean isValidToken(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            if (Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate)) {
                return true;
            }
        }
        return false;
   }

   public String getUsername(String token) {
        Claims claims = getClaims(token);
        if (Objects.nonNull(claims)) {
            return claims.getSubject();
        }
        return null;
   }

   // CODE REVIEW [CODE SMELL]: Retorno null em caso de falha silenciosa ("Returning Null").
   // Problemas:
   //   1. A exceção original é engolida — impossível saber por que o token falhou no debug.
   //   2. Os chamadores isValidToken() e getUsername() precisam verificar null, mas um NPE
   //      pode ocorrer se um chamador futuro esquecer essa verificação.
   //   3. "Returning Null" é um dos code smells mais clássicos (Refactoring, Martin Fowler).
   // Sugestão: retornar Optional<Claims> ou logar a exceção antes de retornar vazio:
   //   } catch (JwtException e) {
   //       log.debug("Token JWT inválido: {}", e.getMessage());
   //       return Optional.empty();
   //   }
   private Claims getClaims(String token) {
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            return null;
        }
   }

}
