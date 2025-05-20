package org.example.wecambackend.config.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.wecambackend.config.auth.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 생성자 주입

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 토큰인증
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트 도메인 명시
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true); // ✔ optional
                    return config;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login_example.html",
                                "/signup_example.html",
                                "/freshman_cert_example.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/public/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
//                .exceptionHandling(exception -> exception
//                    .authenticationEntryPoint((request, response, authException) -> {
//                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                        response.setContentType("application/json");
//                        response.getWriter().write("{\"message\": \"로그인이 필요합니다.\"}");
//                    })
                //Test 용 _ html로 접근되게 함.
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 인증 안 된 사용자 → 프론트 로그인 화면으로 리다이렉트
                            response.sendRedirect("/login_example.html"); // 또는 "/login"
                        })
                );

        return http.build();
    }
}
