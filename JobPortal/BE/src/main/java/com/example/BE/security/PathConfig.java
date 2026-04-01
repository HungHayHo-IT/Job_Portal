package com.example.BE.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class PathConfig {

    @Bean(name = "publicPaths")
    public static List<String> publicPath(){
        return List.of(
                "api/v1/companies/public",
                "api/v1/csrf-token/public",
                "api/v1/auth/register/public",
                "api/v1/auth/login/public",
                "api/v1/contacts/public",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }
    @Bean
    public static List<String> securePath(){
        return List.of(

                "/api/**"
        );
    }

    @Bean(name = "adminPaths")
    public static List<String> adminPaths(){
        return List.of(

                "/api/v1/contacts/admin"
        );
    }
}
