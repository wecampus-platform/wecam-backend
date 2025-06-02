package org.example.wecambackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.common.BaseTimeEntity;
import org.example.wecambackend.model.enums.OrganizationType;
import org.example.wecambackend.model.enums.RequestStatus;

@Entity
@Table(name = "organization_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_pk_id")
    private Long requestId;

    /*organization이 없을 때 null 로 들어가게끔.*/
    //TODO : 외래키 없이 그냥 번호로 해도 될거같은데
    // 번호로 저장하면 무결성 어쩌고의 문제가 발생한다고..
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = true)
    private Organization targetOrganization;

    @Column(name = "school_name", length = 20, nullable = true)
    private String schoolName;

    @Column(name = "college_name", length = 20)
    private String collegeName;

    @Column(name = "department_name", length = 20)
    private String departmentName;

    @Column(name = "council_name", length = 20,  nullable = false)
    private String councilName;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false)
    private OrganizationType organizationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    /*요청자 - ManyToOne 으로 바꾼 이유 : 생각해보니 비대위일때
    그 밑 학과 학생회장이 하는 경우도 있음. 내가 그랬음.*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk_id", nullable = false)
    private User user;
}
