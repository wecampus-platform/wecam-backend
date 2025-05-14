package org.example.wecambackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.common.BaseTimeEntity;
import org.example.wecambackend.model.enums.OrganizationType;
import org.example.wecambackend.model.enums.RequestStatus;

@Entity
@Table(name = "organization_request")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_pk_id")
    private Long requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization targetOrganization;

    @Column(name = "school_name", length = 20, nullable = false)
    private String schoolName;

    @Column(name = "college_name", length = 20)
    private String collegeName;

    @Column(name = "department_name", length = 20)
    private String departmentName;

    @Column(name = "council_name", length = 20, nullable = false)
    private String councilName;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false)
    private OrganizationType organizationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "user_pk_id", nullable = false)
    private Long userPkId;
}
