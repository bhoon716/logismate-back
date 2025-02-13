package hangman.logismate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupRequest {

    private String email;
    private String password;
}
