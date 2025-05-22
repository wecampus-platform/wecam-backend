package org.example.wecambackend.controller.client;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.wecambackend.config.security.UserDetailsImpl;
import org.example.wecambackend.config.security.annotation.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("client/user/mypage")
@Tag(name = "",description = "")
public class UserMyPageController {

    @GetMapping
    public ResponseEntity<MyPageResponse> getMyInfo(@CurrentUser UserDetailsImpl currentUser) {
        return ResponseEntity.ok(myPageService.getMyPageInfo(currentUser));
    }

}
