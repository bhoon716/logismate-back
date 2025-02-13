package hangman.logismate.controller;

import hangman.logismate.dto.ChatMessageDTO;
import hangman.logismate.entity.ChatMessage;
import hangman.logismate.service.ChatService;
import hangman.logismate.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final JwtUtil jwtUtil; // JwtUtil 주입

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO messageDTO,
                            @Header("Authorization") String authHeader) {
        // 헤더에 Authorization 값이 없거나 "Bearer "로 시작하지 않으면 인증 실패 처리
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("WebSocket 인증 실패: 토큰이 없음");
        }
        String token = authHeader.substring(7); // "Bearer " 제거

        // 토큰 검증
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalStateException("WebSocket 인증 실패: 토큰 검증 실패");
        }
        // 토큰에서 userId 추출
        Long currentUserId = jwtUtil.getUserIdFromToken(token);
        messageDTO.setSenderId(currentUserId);

        ChatMessage savedMessage = chatService.saveChatMessage(messageDTO);
        Long recipientId = chatService.getRecipientId(savedMessage.getChatRoomId(), currentUserId);

        // 수신자와 발신자 모두에게 메시지 전송
        messagingTemplate.convertAndSendToUser(recipientId.toString(), "/queue/messages", savedMessage);
        messagingTemplate.convertAndSendToUser(currentUserId.toString(), "/queue/messages", savedMessage);
    }
}
