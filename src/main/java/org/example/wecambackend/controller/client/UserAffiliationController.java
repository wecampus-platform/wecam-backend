package org.example.wecambackend.controller.client;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.wecambackend.config.security.UserDetailsImpl;
import org.example.wecambackend.config.security.annotation.IsUnauth;
import org.example.wecambackend.service.client.Affiliation.AffiliationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/client/user/affiliation")
@RequiredArgsConstructor
@Tag(name = "Client User Affiliation Controller", description = "유저가 재학생/신입생 인증 하는 것")
public class UserAffiliationController {

    private final AffiliationService affiliationService;

    @IsUnauth
    @PostMapping("/freshman")
    @Operation(summary = "신입생 인증 요청", description = "사진을 업로드하고 OCR 추출 결과를 DB에 저장")
    public ResponseEntity<?> registerFreshmanAffiliation(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestPart("file") MultipartFile file
    ) {
        Long userId = userDetails.getId();
        affiliationService.saveNewStudentAffiliation(userId, file);
        return ResponseEntity.ok("신입생 인증 요청이 접수되었습니다.");
    }


}
