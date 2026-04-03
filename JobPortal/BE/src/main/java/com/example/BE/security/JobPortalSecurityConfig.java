package com.example.BE.security;

import com.example.BE.security.filter.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class JobPortalSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf->csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .cors(corsConfig->corsConfig.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests-> {
                    PathConfig.publicPath().forEach(path -> requests.requestMatchers(path).permitAll());
                    PathConfig.adminPaths().forEach(path->requests.requestMatchers(path).hasRole("ADMIN"));
                    PathConfig.securePath().forEach(path -> requests.requestMatchers(path).authenticated());

                })

                .addFilterBefore(new JwtTokenValidatorFilter(PathConfig.publicPath()), BasicAuthenticationFilter.class)
                .exceptionHandling(
                        exception->exception
                                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"Access Denied\", \"message\": \"You don't have permission to access this resource\"}");
                                }))
                                .authenticationEntryPoint(((request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\": \"Access Denied\", \"message\": \"You don't have permission to access this resource\"}");
                                }))
                );


        httpSecurity.formLogin(flc->flc.disable());
        httpSecurity.httpBasic(withDefaults());
        return httpSecurity.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("http://localhost:5173/"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        var user1 = User.builder().username("madan")
//                .password("$2a$10$viS6XrG2FpiZXPQgP7.rQeBrG6TauRaybxsaNjNi.WCLCdIURzZCq")//Madan@123
//                .roles("USER").build();
//        var user2 = User.builder().username("admin")
//                .password("$2a$10$CurDmUEPRQsX5AhEN1NSV.ejUteU0S3dj5XfjxOjaVFhCCTOuj8WG")//Admin@123
//                .roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }

    //Vì class JobPortalUsernamePwd... của bạn có @Component, Spring sẽ tự lấy nó và "nhét" vào tay ông Trưởng phòng này.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider); // chính là đang trả về AuthenticationManager đây class iNterface còn providerManager là class impl class thực thi
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}
