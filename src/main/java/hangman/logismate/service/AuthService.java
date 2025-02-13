package hangman.logismate.service;

import hangman.logismate.dto.SigninRequest;
import hangman.logismate.dto.SigninResponse;
import hangman.logismate.dto.SignupRequest;
import hangman.logismate.dto.SignupResponse;
import hangman.logismate.entity.User;
import hangman.logismate.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import hangman.logismate.fileupload.AwsS3Uploader;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final AwsS3Uploader awsS3Uploader; // AwsS3Uploader 주입


    // 회원가입
    public SignupResponse userSignup(SignupRequest request, MultipartFile companyImage) {
        boolean isExist = userRepository.existsUserByEmail(request.getEmail());
        if(isExist){
            throw new IllegalArgumentException("이미 존재하는 이메일");
        }

        // S3에 이미지를 업로드하고 URL을 반환받음
        String companyImageUrl = null;
        if (companyImage != null && !companyImage.isEmpty()) {
            try {
                companyImageUrl = awsS3Uploader.uploadFile(companyImage); //url임
            } catch (IOException e) {
                throw new IllegalArgumentException("이미지 업로드 실패: " + e.getMessage());
            }
        }

        User user = User.builder()
                .userRole(request.getUserRole())
                .email(request.getEmail())
                .password(request.getPassword())
                .companyName(request.getCompanyName())
                .businessRegistrationNumber(request.getBusinessRegistrationNumber())
                .companyContact(request.getCompanyContact())
                .companyAddress(request.getCompanyAddress())
                .companyImagePath(companyImageUrl)
                .build();

        User saved = userRepository.save(user);

        return SignupResponse.builder()
                .token("token")
                .message("회원가입 성공 : " + saved.getEmail())
                .build();
    }

    // 로그인
    public SigninResponse userSignin(SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("잘못된 이메일 혹은 비밀번호"));

        if(user.getPassword() != request.getPassword()){
            throw new IllegalArgumentException("잘못된 이메일 혹은 비밀번호");
        }

        return SigninResponse.builder()
                .token("token")
                .message("로그인 성공")
                .build();
    }
}
