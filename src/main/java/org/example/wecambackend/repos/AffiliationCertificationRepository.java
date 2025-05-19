package org.example.wecambackend.repos;

import org.example.wecambackend.model.User.User;
//import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffiliationCertificationRepository extends JpaRepository<AffiliationCertification,Long> {

    //유저 당 신입생 인증 한번, 재학생 인증 한번
    boolean existsByUserAndAuthenticationType(User user, AuthenticationType authenticationType);

}
