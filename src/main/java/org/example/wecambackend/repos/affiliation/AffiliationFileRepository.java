package org.example.wecambackend.repos.affiliation;

import org.example.wecambackend.model.affiliation.AffiliationFile;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffiliationFileRepository extends JpaRepository<AffiliationFile,Long> {

    Optional<AffiliationFile> findByAffiliationCertification_Id_UserIdAndAffiliationCertification_Id_AuthenticationType(Long userId, AuthenticationType authType);
}
