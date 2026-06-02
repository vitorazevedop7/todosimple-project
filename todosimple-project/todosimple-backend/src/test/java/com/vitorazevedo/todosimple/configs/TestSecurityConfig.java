package com.vitorazevedo.todosimple.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// CODE REVIEW [TESTES]: Esta configuração desabilita completamente a segurança nos testes
// com .anyRequest().permitAll(). Isso significa que vulnerabilidades de autorização
// (ex.: usuário A acessando dados do usuário B) nunca serão detectadas pelos testes de controller.
// Sugestão: manter a segurança ativa e usar anotações do Spring Security Test:
//   @WithMockUser(username = "user1", roles = {"USER"}) nos métodos de teste
// Isso permite testar cenários de acesso autorizado e não autorizado realisticamente.
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests().anyRequest().permitAll();
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
