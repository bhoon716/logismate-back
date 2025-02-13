package hangman.logismate.controller;

import hangman.logismate.dto.SigninRequest;
import hangman.logismate.dto.SigninResponse;
import hangman.logismate.dto.SignupRequest;
import hangman.logismate.dto.SignupResponse;
import hangman.logismate.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "회원가입 및 로그인 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> userSignup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.userSignup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> userSignin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authService.userSignin(request));
    }
}
