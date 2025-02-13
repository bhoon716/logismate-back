package hangman.logismate.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SigninResponse {

    private String token;
    private String message;
}
