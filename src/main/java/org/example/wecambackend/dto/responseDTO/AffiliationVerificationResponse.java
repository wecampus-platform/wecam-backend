package org.example.wecambackend.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//학생회에게 보여주는 인증서 내용
public class AffiliationVerificationResponse {
    private Long certificationId;                // affiliation_certification 기본키 (엔티티에서 추출)
    private String userName;                     // ocr_user_name
    private String schoolName;                   // ocr_school_name
    private String organizationName;             // ocr_organization_name
    private String enrollYear;                   // ocr_enroll_year
    private String studentEmail;                 // 사용자 이메일 (user join 필요)
    private String authenticationType;           // enum: 'NEW_STUDENT', 'CURRENT_STUDENT'
    private String ocrResult;                    // enum: 'SUCCESS', 'FAILURE', 'UNCLEAR'
    private String status;                       // enum: 'PENDING', 'APPROVED', ...
    private LocalDateTime requestedAt;           // 요청 시각
    private String uploadedFileUrl;              // affiliation_file.file_path → 이미지 등 보여주기 위해

}
