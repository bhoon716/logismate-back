package hangman.logismate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 채팅방에서 발생한 메시지인지 (ChatRoom id)
    private Long chatRoomId;

    // 메시지 보낸 사람 id
    private Long senderId;

    // 텍스트 내용 (없을 수도 있음)
    @Column(columnDefinition = "TEXT")
    private String content;

    // 이미지 URL (없을 수도 있음)
    private String imageUrl;

    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();
}
