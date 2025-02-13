package hangman.logismate.service;

import hangman.logismate.dto.SigninRequest;
import hangman.logismate.dto.SigninResponse;
import hangman.logismate.dto.SignupRequest;
import hangman.logismate.dto.SignupResponse;
import hangman.logismate.entity.User;
import hangman.logismate.repository.UserRepository;
import hangman.logismate.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public SignupResponse userSignup(SignupRequest request) {
        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 🔹 비밀번호 해싱
                .userRole(request.getUserRole()) // 🔹 UserRole 추가
                .companyName(request.getCompanyName())
                .RegisterBusinessNumber(request.getRegisterBusinessNumber())
                .companyContact(request.getCompanyContact())
                .companyAddress(request.getCompanyAddress())
                .companyImage(request.getCompanyImage())
                .build();

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getId());

        return SignupResponse.builder()
                .token(token)
                .message("회원가입 성공: " + savedUser.getEmail())
                .build();
    }

    // 로그인
    public SigninResponse userSignin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 혹은 비밀번호"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { // 🔹 비밀번호 해싱 비교
            throw new IllegalArgumentException("잘못된 이메일 혹은 비밀번호");
        }

        String token = jwtUtil.generateToken(user.getId());

        return SigninResponse.builder()
                .token(token)
                .message("로그인 성공")
                .build();
    }
}
