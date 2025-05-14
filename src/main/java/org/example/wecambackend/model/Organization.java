package org.example.wecambackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.common.BaseTimeEntity;
import org.example.wecambackend.model.enums.OrganizationType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private University university;

    @Column(name = "organization_name", length = 50, nullable = false)
    private String organizationName;

    @Column(name = "level")
    private int level;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type")
    private OrganizationType organizationType;

    // 상위 조직 (자기 자신을 참조하는 FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Organization parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Organization> children = new ArrayList<>();
}
