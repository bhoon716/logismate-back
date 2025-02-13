package hangman.logismate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 두 유저의 id를 저장 (항상 작은 id를 user1Id, 큰 id를 user2Id로 저장)
    @Column(nullable = false)
    private Long user1Id;

    @Column(nullable = false)
    private Long user2Id;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
