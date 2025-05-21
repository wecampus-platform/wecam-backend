package org.example.wecambackend.repos.affiliation;

import org.example.wecambackend.dto.responseDTO.AffiliationVerificationResponse;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.model.User.User;
//import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.affiliation.AffiliationCertificationId;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AffiliationCertificationRepository extends
        JpaRepository<AffiliationCertification, AffiliationCertificationId>{


    //조직별 요청서 확인
    List<AffiliationCertification> findByOrganizationOrganizationIdOrderByRequestedAtDesc(Long organizationId);

    // 유저 당 신입생 인증 한번, 재학생 인증 한번
    boolean existsByUserAndAuthenticationType(User user, AuthenticationType authenticationType);

}