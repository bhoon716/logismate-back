package hangman.logismate.repository;

import hangman.logismate.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방의 전체 메시지 내역(오래된 순으로)
    List<ChatMessage> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);

    // 특정 채팅방의 마지막 메시지 조회
    Optional<ChatMessage> findFirstByChatRoomIdOrderBySentAtDesc(Long chatRoomId);
}