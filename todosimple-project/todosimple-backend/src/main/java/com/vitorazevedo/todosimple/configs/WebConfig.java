package com.vitorazevedo.todosimple.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{

    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // CODE REVIEW [SEGURANÇA]: allowedOrigins("*") permite requisições de qualquer domínio.
        // Em produção isso abre a API para qualquer origem na internet.
        // Sugestão: restringir por perfil usando @Value("${cors.allowed-origins}").
        // Ex.: application-dev.properties -> cors.allowed-origins=http://localhost:3000
        //      application-prod.properties -> cors.allowed-origins=${FRONTEND_URL}
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
    }
    
}
