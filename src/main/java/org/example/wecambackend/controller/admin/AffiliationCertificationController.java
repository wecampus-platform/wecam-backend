package org.example.wecambackend.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.wecambackend.config.security.annotation.IsCouncil;
import org.example.wecambackend.dto.responseDTO.AffiliationVerificationResponse;
import org.example.wecambackend.exception.UnauthorizedException;
import org.example.wecambackend.model.Council;
import org.example.wecambackend.repos.affiliation.AffiliationCertificationRepository;
import org.example.wecambackend.service.admin.AffiliationCertificationAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/council/{councilname}/affiliation")
//@RequestMapping("/admin")
//@IsCouncil
@Tag(name = "Affiliation Certification Controller", description = "학생회 관리자 페이지 안에서 소속 인증 관련 정보")
public class AffiliationCertificationController {
    private final AffiliationCertificationAdminService affiliationCertificationAdminService;

    @GetMapping("/requests")
    @Tag(name = "Affiliation Certification Controller", description = "학생회 관리자 페이지 안에서 소속 인증 요청 확인 하기 버튼을 눌렀을 때 보이는 조회셀_조직별로 신청서 조회이므로, 인증요청서 타입별 뷰는 프론트에서 해야함.")
    public ResponseEntity<List<AffiliationVerificationResponse>> getAffiliationRequests(
            @PathVariable String councilName, // ← 화면용
            @RequestParam("councilId") Long councilId
    ) {
        List<AffiliationVerificationResponse> list =
                affiliationCertificationAdminService.getRequestsByCouncilId(councilId);


        return ResponseEntity.ok(list);
    }

//    @GetMapping("/test/requests")
//    @Tag(name = "test", description = "학생회 관리자 페이지 안에서 소속 인증 요청 확인 하기 버튼을 눌렀을 때 보이는 조회셀_조직별로 신청서 조회이므로, 인증요청서 타입별 뷰는 프론트에서 해야함.")
//    public ResponseEntity<List<AffiliationVerificationResponse>> testGetAffiliationRequests() {
//        // Step 1: 임시 조직 ID
//        Long mockOrganizationId = 303L;
//
//        // Step 2: 서비스 호출
//        return ResponseEntity.ok(
//                affiliationCertificationAdminService.getRequestsForOrganization(mockOrganizationId)
//        );
//    }


}
