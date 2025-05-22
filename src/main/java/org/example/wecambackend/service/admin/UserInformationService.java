package org.example.wecambackend.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.User.UserInformation;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.example.wecambackend.repos.UserInformationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserInformationService {


    private final UserInformationRepository userInformationRepository;

    public void createUserInformation(User user, AffiliationCertification cert, AuthenticationType type) {
        UserInformation info = userInformationRepository.findByUser(user)
                .orElse(null); // 있으면 수정하는 방향 , 없으면 생성
        if (info == null) {
            info = UserInformation.builder()
                    .user(user)
                    .name(cert.getUsername())
                    .university(cert.getUniversity())
                    .isAuthentication(Boolean.TRUE)
                    .studentId(cert.getOcrEnrollYear())
                    .isCouncilFee(Boolean.FALSE)
                    .build();
            userInformationRepository.save(info);
        }

    }
}
