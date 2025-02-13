package hangman.logismate.dto;

import hangman.logismate.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {

    private UserRole userRole;

    private String email;

    private String password;

    private String companyName;

    private String BusinessRegistrationNumber;

    private String companyContact;

    private String companyAddress;

    // 포워더 회사 소개 이미지 파일 경로
    private String companyImagePath;
}
