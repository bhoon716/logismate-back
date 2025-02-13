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
    private String RegisterBusinessNumber;
    private String companyContact;
    private String companyAddress;
    private String companyImage;
}
