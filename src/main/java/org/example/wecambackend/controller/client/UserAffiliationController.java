package org.example.wecambackend.controller.client;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/user/affiliation")
@RequiredArgsConstructor
@Tag(name = "Client User Affiliation Controller", description = "유저가 재학생 인증 하는 것")
public class UserAffiliationController {


}
