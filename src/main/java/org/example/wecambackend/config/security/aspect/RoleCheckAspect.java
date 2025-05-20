package org.example.wecambackend.config.security.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.wecambackend.config.security.UserDetailsImpl;
import org.example.wecambackend.model.enums.UserRole;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class RoleCheckAspect {

    @Before("@annotation(org.example.wecambackend.config.security.annotation.IsStudent)")
    public void checkStudent() {
        checkUserRole(UserRole.STUDENT);
    }

    @Before("@annotation(org.example.wecambackend.config.security.annotation.IsCouncil)")
    public void checkCouncil() {
        UserRole role = getCurrentUserRole();
        if (role != UserRole.COUNCIL && role != UserRole.ADMIN) {
            throw new AccessDeniedException("학생회만 접근 가능합니다.");
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

    private void checkUserRole(UserRole requiredRole) {
        if (getCurrentUserRole() != requiredRole) {
            throw new AccessDeniedException(requiredRole.name() + "만 접근할 수 있습니다.");
        }
    }
}
