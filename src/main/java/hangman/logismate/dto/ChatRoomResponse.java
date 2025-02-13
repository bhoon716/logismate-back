package hangman.logismate.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatRoomResponse {

    private Long chatRoomId;
    private Long recipientId;      // 현재 로그인 사용자가 아닌 상대방 id
    private String lastMessage;    // 마지막 메시지 (텍스트와 이미지 정보 포함)
    private LocalDateTime lastMessageTime;
}
