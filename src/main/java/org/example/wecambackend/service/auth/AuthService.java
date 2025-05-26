package org.example.wecambackend.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.common.exceptions.BaseException;
import org.example.wecambackend.common.response.BaseResponseStatus;
import org.example.wecambackend.config.auth.JwtTokenProvider;
import org.example.wecambackend.dto.auth.EmailDuplicateCheckResponse;
import org.example.wecambackend.dto.auth.EmailPhoneDuplicateCheckResponse;
import org.example.wecambackend.dto.auth.PhoneDuplicateCheckResponse;
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
        User user = userRepository.findByEmailWithPrivate(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 비밀번호 검증
        String raw = request.getPassword();
        String encoded = user.getUserPrivate().getPassword();

        if (!passwordEncoder.matches(raw, encoded)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String role = user.getRole().name();



        // JWT 발급
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail(), role);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        // RefreshToken Redis 저장
        redisTemplate.opsForValue().set("RT:" + user.getUserPkId(), refreshToken, 7, TimeUnit.DAYS);

        System.out.println("User 로그인 완료 :"+accessToken);
        System.out.println("user pk id : "+user.getUserPkId() +" user email : " +user.getEmail());

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


    public EmailDuplicateCheckResponse validateDuplicatedEmail(String email) {
        boolean isDuplicate = userRepository.existsByEmail(email);
        if (isDuplicate) {
            throw new BaseException(BaseResponseStatus.EMAIL_DUPLICATED,
                    new EmailDuplicateCheckResponse(true));
        }
        return new EmailDuplicateCheckResponse(false);
    }

    public PhoneDuplicateCheckResponse validateDuplicatedPhoneNumber(String phone) {
        String encryptedPhone = phoneEncryptor.encrypt(phone);
        boolean isDuplicate = userPrivateRepository.existsByPhoneNumber(encryptedPhone);
        if (isDuplicate) {
            throw new BaseException(BaseResponseStatus.PHONE_DUPLICATED,
                    new PhoneDuplicateCheckResponse(true));
        }
        return new PhoneDuplicateCheckResponse(false);
    }

    public EmailPhoneDuplicateCheckResponse validateDuplicatedBoth(String email, String phone) {
        boolean emailDup = userRepository.existsByEmail(email);
        String encryptedPhone = phoneEncryptor.encrypt(phone);
        boolean phoneDup = userPrivateRepository.existsByPhoneNumber(encryptedPhone);

        if (emailDup && phoneDup) {
            throw new BaseException(BaseResponseStatus.EMAIL_PHONE_DUPLICATED,
                    new EmailPhoneDuplicateCheckResponse(true, true));
        } else if (emailDup) {
            throw new BaseException(BaseResponseStatus.EMAIL_DUPLICATED,
                    new EmailPhoneDuplicateCheckResponse(true, false));
        } else if (phoneDup) {
            throw new BaseException(BaseResponseStatus.PHONE_DUPLICATED,
                    new EmailPhoneDuplicateCheckResponse(false, true));
        }

        return new EmailPhoneDuplicateCheckResponse(false, false);
    }
}
