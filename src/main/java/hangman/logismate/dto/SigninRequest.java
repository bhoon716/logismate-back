package hangman.logismate.dto;

import hangman.logismate.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninRequest {

    private String email;
    private String password;
    private UserRole userRole;
}
