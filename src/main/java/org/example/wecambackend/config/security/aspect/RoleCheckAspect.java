package org.example.wecambackend.config.security.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.wecambackend.config.security.UserDetailsImpl;
import org.example.wecambackend.exception.UnauthorizedException;
import org.example.wecambackend.model.enums.UserRole;
import org.example.wecambackend.repos.CouncilMemberRepository;
import org.example.wecambackend.service.client.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {
    private final UserService userService;
    private final CouncilMemberRepository councilMemberRepository;

    @Before("@annotation(org.example.wecambackend.config.security.annotation.IsStudent)")
    public void checkStudent() {
        checkUserRole(UserRole.STUDENT);
    }

    //학생회 관리자 페이지 (해당 조직의 학생회의 멤버인지, user_role또한 확인)
    @Before("@annotation(org.example.wecambackend.config.security.annotation.IsCouncil)")
    public void checkCouncil() {
        UserDetailsImpl currentUser = getCurrentUser(); // 로그인된 유저 불러오기
        if (currentUser.getRole() != UserRole.COUNCIL) {
            throw new AccessDeniedException("학생회 권한이 없습니다.");
        }
        boolean isCouncilMember = councilMemberRepository.existsByUserUserPkIdAndCouncil_Organization_organizationIdAndIsActiveTrue(
                currentUser.getId(), currentUser.getOrganizationId());

        if (!isCouncilMember) {
            throw new AccessDeniedException("해당 조직의 학생회 구성원이 아닙니다.");
        }
    }

    //신입생 소속인증 진행은 완료되면 다시 진행하지 못함.(UnAuth -> Guest_Student 에서 사용)
    @Before("@annotation(org.example.wecambackend.config.security.annotation.IsUnauth)")
    public void checkUnauth() {
        checkUserRole(UserRole.UNAUTH);
    }

    //재학생 소속인증 진행은 신입생 인증을 완료한 Guest_Student , Unauth 만 진행이 가능
    @Before("@annotation(org.example.wecambackend.config.security.annotation.IsUnStudent)")
    public void checkUnStudent() {
        UserRole role = getCurrentUserRole();
        if (role != UserRole.GUEST_STUDENT && role != UserRole.UNAUTH) {
            throw new AccessDeniedException("접근이 불가합니다.");
        }
    }

    private UserRole getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("인증되지 않은 사용자입니다.");
        }

        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        return user.getRole();
    }

    private UserDetailsImpl getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userDetails;
    }


    private void checkUserRole(UserRole requiredRole) {
        if (getCurrentUserRole() != requiredRole) {
            throw new AccessDeniedException(requiredRole.name() + "만 접근할 수 있습니다.");
        }
    }


}
