package org.example.wecambackend.model.User;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.common.BaseTimeEntity;

@Entity
@Table(name = "user_signup_information")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupInformation extends BaseTimeEntity {




    @Id
    @Column(name = "user_pk_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_pk_id")
    private User user;

    @Column(name = "select_organization_id")
    private Long selectOrganizationId;

    @Column(name = "select_school_id")
    private Long selectSchoolId;

    @Column(name = "enroll_year", length = 4)
    private String enrollYear;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "input_college_name", length = 20)
    private String inputCollegeName;

    @Column(name = "input_school_name", length = 20)
    private String inputSchoolName;

    @Column(name = "input_department_name", length = 20)
    private String inputDepartmentName;

    @Column(name = "is_make_workspace")
    @Builder.Default
    private Boolean isMakeWorkspace = false;

}
