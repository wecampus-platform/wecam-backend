package org.example.wecambackend.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.config.auth.JwtTokenProvider;
import org.example.wecambackend.dto.auto.LoginRequest;
import org.example.wecambackend.dto.auto.LoginResponse;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.repos.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public LoginResponse login(LoginRequest request) {

        // 1. 이메일로 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 검증
        String raw = request.getPassword();
        String encoded = user.getUserPrivate().getPassword();

        if (!passwordEncoder.matches(raw, encoded)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String role = user.isAuthentication()
                ? user.getUserInformation().getRole().name()
                : "UNAUTH";


        // JWT 발급
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), role);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        // RefreshToken Redis 저장
        redisTemplate.opsForValue().set("RT:" + user.getUserPkId(), refreshToken, 7, TimeUnit.DAYS);

        // 응답 반환
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(role)
                .build();
    }
}
