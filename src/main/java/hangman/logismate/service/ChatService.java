package hangman.logismate.service;

import hangman.logismate.dto.ChatMessageDTO;
import hangman.logismate.dto.ChatRoomResponse;
import hangman.logismate.entity.ChatMessage;
import hangman.logismate.entity.ChatRoom;
import hangman.logismate.repository.ChatMessageRepository;
import hangman.logismate.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 1:1 채팅방 생성 또는 기존 채팅방 반환 (항상 user1Id < user2Id)
    public ChatRoom getOrCreateChatRoom(Long userId1, Long userId2) {
        Long minId = Math.min(userId1, userId2);
        Long maxId = Math.max(userId1, userId2);
        Optional<ChatRoom> chatRoomOpt = chatRoomRepository.findByUser1IdAndUser2Id(minId, maxId);
        return chatRoomOpt.orElseGet(() -> {
            ChatRoom newRoom = ChatRoom.builder()
                    .user1Id(minId)
                    .user2Id(maxId)
                    .createdAt(LocalDateTime.now())
                    .build();
            return chatRoomRepository.save(newRoom);
        });
    }

    // 메시지 저장 (서버에서 sentAt을 재설정)
    public ChatMessage saveChatMessage(ChatMessageDTO dto) {
        ChatMessage message = ChatMessage.builder()
                .chatRoomId(dto.getChatRoomId())
                .senderId(dto.getSenderId())
                .content(dto.getContent())
                .imageUrl(dto.getImageUrl())
                .sentAt(LocalDateTime.now())
                .build();
        return chatMessageRepository.save(message);
    }

    // 특정 채팅방의 전체 메시지를 오래된 순으로 조회
    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId);
    }

    // 로그인 유저가 참여한 채팅방 목록 조회 (각 채팅방의 마지막 메시지와 시각 포함)
    public List<ChatRoomResponse> getChatRoomsForUser(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUser1IdOrUser2Id(userId, userId);
        List<ChatRoomResponse> responses = new ArrayList<>();
        for (ChatRoom room : chatRooms) {
            ChatRoomResponse response = new ChatRoomResponse();
            response.setChatRoomId(room.getId());
            // 현재 로그인 유저와 다른 쪽의 id 설정
            Long recipientId = room.getUser1Id().equals(userId) ? room.getUser2Id() : room.getUser1Id();
            response.setRecipientId(recipientId);

            // 마지막 메시지 조회
            Optional<ChatMessage> lastMsgOpt = chatMessageRepository.findFirstByChatRoomIdOrderBySentAtDesc(room.getId());
            if (lastMsgOpt.isPresent()) {
                ChatMessage lastMsg = lastMsgOpt.get();
                String displayMessage = "";
                if (lastMsg.getContent() != null && !lastMsg.getContent().isEmpty()) {
                    displayMessage = lastMsg.getContent();
                }
                if (lastMsg.getImageUrl() != null && !lastMsg.getImageUrl().isEmpty()) {
                    displayMessage += " [이미지 첨부]";
                }
                response.setLastMessage(displayMessage);
                response.setLastMessageTime(lastMsg.getSentAt());
            }
            responses.add(response);
        }
        // 마지막 메시지 시간 내림차순 정렬 (메시지 없는 채팅방은 뒤쪽)
        responses.sort((a, b) -> {
            if (a.getLastMessageTime() == null && b.getLastMessageTime() == null) return 0;
            if (a.getLastMessageTime() == null) return 1;
            if (b.getLastMessageTime() == null) return -1;
            return b.getLastMessageTime().compareTo(a.getLastMessageTime());
        });
        return responses;
    }

    // 주어진 채팅방에서 현재 전송자와 반대쪽의 userId 반환
    public Long getRecipientId(Long chatRoomId, Long senderId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        return room.getUser1Id().equals(senderId) ? room.getUser2Id() : room.getUser1Id();
    }
}
