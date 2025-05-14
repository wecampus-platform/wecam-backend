package org.example.wecambackend.model.affiliation;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.model.University;
import org.example.wecambackend.model.User.User;
import org.example.wecambackend.model.enums.AuthenticationStatus;
import org.example.wecambackend.model.enums.AuthenticationType;
import org.example.wecambackend.model.enums.OcrResult;

import java.time.LocalDateTime;

@Entity
@Table(name = "affiliation_certification")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AffiliationCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authentication_pk_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ocr_result", nullable = false)
    private OcrResult ocrResult;

    @Column(name = "ocr_user_name", length = 20)
    private String ocrUserName;

    @Column(name = "ocr_enroll_year", length = 4)
    private String ocrEnrollYear;

    @Column(name = "ocr_school_name", length = 20)
    private String ocrSchoolName;

    @Column(name = "ocr_organization_name", length = 20)
    private String ocrOrganizationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuthenticationStatus status;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "upload_user_pk_id")
    private User user;

    @Column(name = "pk_reviewer_infromation_id")
    private Long reviewerInformationPkId;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "school_pk_id")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "organization_pk_id")
    private Organization organization;

    @Column(name = "user_pk_id", nullable = false)
    private Long userPkId;


    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", nullable = false)
    private AuthenticationType authenticationType;
}
