package org.example.wecambackend.model.affiliation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.example.wecambackend.model.enums.AuthenticationType;

import java.io.Serializable;
import java.util.Objects;

// 복합키 설정 , 유저 한명 당 재학생 인증 한번, 신입생 인증 한번
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class AffiliationCertificationId implements Serializable {

    @Column(name = "pk_upload_userid")
    private Long userId;

    @Column(name = "authentication_type")
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    // 기본 생성자, equals, hashCode 필수
    public AffiliationCertificationId() {}

    public AffiliationCertificationId(Long userId, AuthenticationType authenticationType) {
        this.userId = userId;
        this.authenticationType = authenticationType;
    }

    // equals & hashCode (중요)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AffiliationCertificationId)) return false;
        AffiliationCertificationId that = (AffiliationCertificationId) o;
        return Objects.equals(userId, that.userId) &&
                authenticationType == that.authenticationType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, authenticationType);
    }
}
