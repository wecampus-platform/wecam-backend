package org.example.wecambackend.service.client.Affiliation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.wecambackend.dto.responseDTO.OcrResultResponse;
import org.example.wecambackend.exception.DuplicateSubmissionException;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.model.University;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserSignupInformation;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.affiliation.AffiliationCertificationId;
import org.example.wecambackend.model.enums.AuthenticationStatus;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.example.wecambackend.model.enums.OcrResult;
import org.example.wecambackend.repos.*;
import org.example.wecambackend.repos.affiliation.AffiliationCertificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
public class AffiliationService {

    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;
    private final AffiliationFileService affiliationFileService;
    private final OcrService ocrService;
    private final AffiliationCertificationRepository affiliationCertificationRepository;
    private final UserSignupInformationRepository userSignupInformationRepository;
    private final SchoolRepository schoolRepository;
    private final OrganizationRepository organizationRepository;

    /*TODO : 좋지 않은 접속을 막기 위해 서비스 딴에 해뒀지만, 상태저장으로 UI/UX 버튼 비활성화도 필요.
       신입생 인증 진행 시 role 변환을 할 거니까 적용됨. 다만, ROle UPDATE 시점은 승인 후 이기 떄문에, 그전까지는 버튼이 비활성화 되지 않음.
       이거또한 UI에 구현할 것인지 얘기 해봐야한다.*/

    // Student= New 랑 Current 합침!
    //DB 한꺼번에 저장. 업로드되자마자 OCR 결과추출해서 Affiliation 테이블에 값이 들어가는 거 까지가 하나의 로직
    @Operation(summary = "인증서 등록 서비스", description = "사진 저장, OCR 추출 결과를 DB에 저장 전체 Transactional로 묶임.")
    @Transactional
    public void saveStudentAffiliation(Long userId, MultipartFile file, AuthenticationType status) {

        //1. 유저조회
        User uploadUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        // 2. 이미 인증 요청이 존재하는지 확인
        boolean exists = affiliationCertificationRepository.existsByUserAndAuthenticationType(uploadUser, status);
        if (exists) {
            throw new DuplicateSubmissionException("이미 해당 유형의 인증 요청을 제출하셨습니다.");
        }

        // 3. 회원가입 정보 조회
        UserSignupInformation signupInfo = userSignupInformationRepository.findByUser_UserPkId(userId)
                .orElseThrow(() -> new RuntimeException("회원가입 정보 없음"));

        // 4. 학교/소속 정보 조회
        University school = schoolRepository.findById(signupInfo.getSelectSchoolId())
                .orElseThrow(() -> new RuntimeException("학교 정보 없음"));

        Organization organization = organizationRepository.findById(signupInfo.getSelectOrganizationId())
                .orElseThrow(() -> new RuntimeException("소속 정보 없음"));

        // 5. OCR 수행 → 결과 DTO로 매핑
        Map<String, String> result = ocrService.requestOcr(file);
        OcrResultResponse ocrResultDto = OcrResultResponse.builder()
                .userName(result.get("userName"))
                .schoolName(result.get("schoolName"))
                .orgName(result.get("orgName"))
                .enrollYear(result.get("enrollYear"))
                .build();

        // 6. OCR 결과 판단
        OcrResult ocrResult = determineFreshmanOcrResult(signupInfo, ocrResultDto, school, organization);


        AffiliationCertification cert;
        // 7. 인증 정보 저장 - 신입생 인증 정보 저장

        AffiliationCertificationId id = new AffiliationCertificationId(
                uploadUser.getUserPkId(),
                status);


         cert = AffiliationCertification.builder()
                .id(id)
                .user(uploadUser)
                .authenticationType(status)
                .ocrUserName(ocrResultDto.getUserName())
                .ocrEnrollYear(ocrResultDto.getEnrollYear())
                .ocrSchoolName(ocrResultDto.getSchoolName())
                .ocrOrganizationName(ocrResultDto.getOrgName())
                .ocrResult(ocrResult)
                .status(AuthenticationStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .organization(organization)
                .university(school)
                .build();

        affiliationCertificationRepository.save(cert);
        // 8. 파일 정보 저장
        UUID uuid = UUID.randomUUID();
        String path = fileStorageService.save(file,uuid);
        affiliationFileService.saveToDB(cert, file, path,uuid);

    }

    //신입생 OCR 결과값 평가
    public OcrResult determineFreshmanOcrResult(UserSignupInformation signupInfo,
                                        OcrResultResponse ocrResult,
                                        University school,
                                        Organization org) {

        int matchCount = 0;

        // 1. 이름 비교
        if (equalsIgnoreCaseSafe(signupInfo.getName(), ocrResult.getUserName())) matchCount++;

        // 2. 입학년도 비교
        if (signupInfo.getEnrollYear().equals(ocrResult.getEnrollYear())) matchCount++;

        // 3. 학교 이름 비교 (부분 일치 허용)
        if (school.getSchoolName().contains(ocrResult.getSchoolName())) matchCount++;

        // 4. 소속 이름 비교 (부분 일치 허용)
        if (org.getOrganizationName().contains(ocrResult.getOrgName())) matchCount++;

        // OCR 실패 판단 (결과값 자체가 비어 있는 경우)
        if (Stream.of(
                ocrResult.getUserName(),
                ocrResult.getSchoolName(),
                ocrResult.getOrgName(),
                ocrResult.getEnrollYear()
        ).anyMatch(s -> s == null || s.isBlank())
        ) {
            return OcrResult.FAILURE;
        }

        // 판단 기준
        if (matchCount == 4) return OcrResult.SUCCESS;
        else if (matchCount >= 1) return OcrResult.UNCLEAR;
        else return OcrResult.FAILURE;
    }

    private boolean equalsIgnoreCaseSafe(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }
}
