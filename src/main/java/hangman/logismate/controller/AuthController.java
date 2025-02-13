package hangman.logismate.controller;

import hangman.logismate.dto.SigninRequest;
import hangman.logismate.dto.SigninResponse;
import hangman.logismate.dto.SignupRequest;
import hangman.logismate.dto.SignupResponse;
import hangman.logismate.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Authentication Controller", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> userSignup(
            @RequestBody SignupRequest request,    // JSON 데이터를 받음
            @RequestParam("companyImage") MultipartFile companyImage) {    // 이미지 파일을 받음
        return ResponseEntity.ok(authService.userSignup(request, companyImage));
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> userSignin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authService.userSignin(request));
    }
}
