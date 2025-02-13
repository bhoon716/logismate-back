package hangman.logismate.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageDTO {

    private Long chatRoomId;
    private Long senderId;  // 서버에서 JWT 정보를 통해 채워줌
    private String content;
    private String imageUrl;  // 이미지 첨부 시 URL 또는 파일 경로
    private LocalDateTime sentAt; // 서버에서 설정
}
