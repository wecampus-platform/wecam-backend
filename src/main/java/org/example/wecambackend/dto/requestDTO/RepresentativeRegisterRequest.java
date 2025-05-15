package org.example.wecambackend.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RepresentativeRegisterRequest {
    private String email;
    private String password;
    private String phoneNumber;

    private String name;

    //대표자일때 사용 가능해짐.
    private String inputSchoolName;
    private String inputCollegeName;
    private String inputDepartmentName;


    private String enrollYear;

    //대표자일 때도 선택할 경우가 있으니까
    private Long selectSchoolId;
    private Long selectOrganizationId;
}
