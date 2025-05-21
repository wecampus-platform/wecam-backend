package org.example.wecambackend.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.dto.projection.AffiliationFileProjection;
import org.example.wecambackend.dto.responseDTO.AffiliationVerificationResponse;
import org.example.wecambackend.model.Council;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.affiliation.AffiliationCertificationId;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.example.wecambackend.repos.CouncilRepository;
import org.example.wecambackend.repos.affiliation.AffiliationCertificationRepository;
import org.example.wecambackend.repos.affiliation.AffiliationFileRepository;
import org.springframework.stereotype.Service;
import org.example.wecambackend.model.affiliation.AffiliationFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AffiliationCertificationAdminService {
    private final AffiliationCertificationRepository affiliationCertificationRepository;
    private final AffiliationFileRepository affiliationFileRepository;
    private final CouncilRepository councilRepository;
    public List<AffiliationVerificationResponse> getRequestsForOrganization(Long organizationId) {
        List<AffiliationCertification> certifications =
                affiliationCertificationRepository.findByOrganizationOrganizationIdOrderByRequestedAtDesc(organizationId);

        return certifications.stream().map(ac -> {
            // 복합키 구성 정보
            Long userId = ac.getId().getUserId();
            AuthenticationType authenticationType = ac.getId().getAuthenticationType();
            AffiliationCertificationId id = ac.getId();
//            Optional<AffiliationFile> optionalFile = affiliationFileRepository
//                    .findByUserIdAndAuthOrdinal(userId,ac.getAuthenticationType().ordinal());
            Optional<AffiliationFileProjection> optionalFile = affiliationFileRepository.findFilePathAndNameByUserIdAndAuthOrdinal(userId, authenticationType.ordinal());
            System.out.println("조회된 파일: " + optionalFile);
            String filePath = optionalFile.map(file -> file.getFilePath()).orElse(null);

            return new AffiliationVerificationResponse(
                    userId,
                    authenticationType.name(),
                    ac.getOcrUserName(),
                    ac.getOcrSchoolName(),
                    ac.getOcrOrganizationName(),
                    ac.getOcrEnrollYear(),
                    ac.getOcrResult().name(),
                    ac.getStatus().name(),
                    ac.getRequestedAt(),
                    filePath
            );
        }).toList();
    }

    public List<AffiliationVerificationResponse> getRequestsByCouncilId(Long councilId) {
        Council council = councilRepository.findById(councilId)
                .orElseThrow(() -> new RuntimeException("해당 학생회를 찾을 수 없습니다."));

        Long organizationId = council.getOrganization().getOrganizationId();

        return getRequestsForOrganization(organizationId);
    }
}
