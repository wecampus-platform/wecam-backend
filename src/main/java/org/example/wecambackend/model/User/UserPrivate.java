package org.example.wecambackend.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_private")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPrivate {

    @Id
    @Column(name = "user_pk_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_pk_id")
    private User user;

    @Column(name = "phone_number",length = 150, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password", length = 100, nullable = false)
    private String password;
}
