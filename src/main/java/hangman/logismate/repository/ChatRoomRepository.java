package hangman.logismate.repository;

import hangman.logismate.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 두 유저 간 채팅방 존재 여부 (항상 작은 id가 user1Id에 저장되도록 처리)
    Optional<ChatRoom> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    // 특정 유저가 참여한 채팅방 조회
    List<ChatRoom> findByUser1IdOrUser2Id(Long user1Id, Long user2Id);
}
