package org.example.wecambackend.dto.responseDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.wecambackend.model.enums.UserRole;

@Getter
@AllArgsConstructor
public class MyPageResponse {
    private String username; //이름
    private String phoneNumber; //전화번호 - 연락처
    private String userEmail; //아이디 이메일
    private Long universityId; //학교 아이디
    private Long organizationId; // 학교 단과대학 학과 이름
    private Boolean academicStatus; //학적 상태
    private UserRole role;
    private int student_grade;

}
