package org.example.wecambackend.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.config.auth.JwtTokenProvider;
import org.example.wecambackend.dto.auto.LoginRequest;
import org.example.wecambackend.dto.auto.LoginResponse;
import org.example.wecambackend.dto.requestDTO.StudentRegisterRequest;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserPrivate;
import org.example.wecambackend.model.User.UserSignupInformation;
import org.example.wecambackend.repos.UserPrivateRepository;
import org.example.wecambackend.repos.UserRepository;
import org.example.wecambackend.repos.UserSignupInformationRepository;
import org.example.wecambackend.util.PhoneEncryptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final PhoneEncryptor phoneEncryptor;

    private final UserPrivateRepository userPrivateRepository;
    private final UserSignupInformationRepository signupInfoRepository;


    //login
    public LoginResponse login(LoginRequest request) {

        // 이메일 유저 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
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


    //일반유저_회원가입
    @Transactional
    public void registerStudent(StudentRegisterRequest req) {

        //이메일 중복 체크
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        //user 생성
        User user = User.builder()
                .email(req.getEmail())
                .build();
        userRepository.save(user);

        //양방향 핸드폰 번호
        String encryptedPhone = phoneEncryptor.encrypt(req.getPhoneNumber());

        //user_private 저장
        UserPrivate userPrivate = UserPrivate.builder()
                .user(user)
                .password(passwordEncoder.encode(req.getPassword()))
                .phoneNumber(encryptedPhone)
                .build();
        userPrivateRepository.save(userPrivate);

        //user_signup_information 저장
        UserSignupInformation signupInfo = UserSignupInformation.builder()
                .user(user)
                .name(req.getName())
                .enrollYear(req.getEnrollYear())
                .selectSchoolId(req.getSelectSchoolId())
                .selectOrganizationId(req.getSelectOrganizationId())
                .isMakeWorkspace(false)
                .build();
        signupInfoRepository.save(signupInfo);
    }


}
