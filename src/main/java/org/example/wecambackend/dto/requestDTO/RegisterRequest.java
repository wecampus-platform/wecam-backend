package org.example.wecambackend.dto.requestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
}
