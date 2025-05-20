package org.example.wecambackend.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.dto.responseDTO.AffiliationVerificationResponse;
import org.example.wecambackend.model.Council;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
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
            AuthenticationType authType = ac.getId().getAuthenticationType();

            Optional<AffiliationFile> optionalFile = affiliationFileRepository
                    .findByAffiliationCertification_Id_UserIdAndAffiliationCertification_Id_AuthenticationType(userId, authType);

            String filePath = optionalFile.map(file -> file.getFilePath()).orElse(null);

            return new AffiliationVerificationResponse(
                    userId,
                    authType.name(),
                    ac.getOcrUserName(),
                    ac.getOcrSchoolName(),
                    ac.getOcrOrganizationName(),
                    ac.getOcrEnrollYear(),
                    ac.getUser().getEmail(),
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
