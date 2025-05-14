package org.example.wecambackend.model.User;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.model.University;
import org.example.wecambackend.model.common.BaseTimeEntity;
import org.example.wecambackend.model.enums.AcademicStatus;
import org.example.wecambackend.model.enums.UserRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInformation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "information_pk_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk_id", nullable = false)
    private User user;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "school_email", unique = true)
    private String schoolEmail;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "school_id")
    private University university;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "student_grade")
    private Byte studentGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_status")
    private AcademicStatus academicStatus;

    @Column(name = "nickname", length = 20, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Column(name = "is_superuser", nullable = false)
    private Boolean isSuperuser;

    @Column(name = "is_authentication", nullable = false)
    private Boolean isAuthentication;

    @Column(name = "is_council_fee", nullable = false)
    private Boolean isCouncilFee;

    @Column(name = "password_update_at")
    private LocalDateTime passwordUpdateAt;

    public void updatePassword() {
        this.passwordUpdateAt = LocalDateTime.now();
    }


    // 생략된 부분 추가 가능
}
