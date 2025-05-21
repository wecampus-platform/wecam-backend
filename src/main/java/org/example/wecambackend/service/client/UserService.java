package org.example.wecambackend.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.model.University;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.affiliation.AffiliationCertification;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.example.wecambackend.model.enums.UserRole;
import org.example.wecambackend.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public void updateUserRoleAndStatus(User user, Organization organization, University university, AuthenticationType authenticationType) {
        UserRole beforeRole = user.getRole();
        if (authenticationType == AuthenticationType.NEW_STUDENT) {
            user.setRole(UserRole.GUEST_STUDENT); // enum 기반
        }
        else {
            user.setRole(UserRole.STUDENT);
        }
        if (!beforeRole.equals(user.getRole())) {
            user.setExpiresAt(null); // TODO : 우선 ROLE 이 바뀌면 ExpiresAt은 비활성화 시켰음.
        }
        user.setUniversity(university);
        user.setOrganization(organization);

        userRepository.save(user);
        log.info("[유저 상태 변경] {}: {} → {}, 학교: {}, 조직: {}",
                user.getEmail(),
                beforeRole.name(),
                user.getRole().name(),
                university.getSchoolName(),
                organization.getOrganizationName());
    }
}
