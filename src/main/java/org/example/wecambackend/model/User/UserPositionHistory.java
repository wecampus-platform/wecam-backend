package org.example.wecambackend.model.User;

import jakarta.persistence.*;
import lombok.*;
import org.example.wecambackend.model.Council;
import org.example.wecambackend.model.Organization;
import org.example.wecambackend.model.enums.MemberRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_position_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPositionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_pk_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Column(name = "member_type", length = 20)
    private String memberType;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "user_pk_id", nullable = false)
    private Long userPkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "council_id", nullable = false)
    private Council council;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @Column(name = "organization_id", nullable = false)
    private Organization organization;
}
