package org.example.wecambackend.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk_id")
    private Long userPkId;


    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_authentication", nullable = false)
    private boolean isAuthentication;

    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private UserPrivate userPrivate;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserInformation userInformation;


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.expiresAt = now.plusDays(30);
        this.isAuthentication = false;
        this.isEmailVerified = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}