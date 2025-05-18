package org.example.wecambackend.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login_example.html",
                                "/signup_example.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/client/auth/**", // 로그인/회원가입 API 허용
                                "/swagger-ui/**", // Swagger UI HTML/CSS/JS 경로
                                "/v3/api-docs/**", // OpenAPI JSON 경로
                                "/swagger-resources/**", // (일부 swagger-ui 라이브러리)
                                "/webjars/**", // swagger-ui에 필요한 js 라이브러리
                                "/public/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()); // 기본 로그인 화면 비활성화

        return http.build();
    }


}