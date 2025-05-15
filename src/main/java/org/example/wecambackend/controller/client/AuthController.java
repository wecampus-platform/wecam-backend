package org.example.wecambackend.controller.client;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.dto.requestDTO.StudentRegisterRequest;
import org.example.wecambackend.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegisterRequest request) {
        authService.registerStudent(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}