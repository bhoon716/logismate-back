package hangman.logismate.controller;

import hangman.logismate.dto.ChatRoomResponse;
import hangman.logismate.entity.ChatMessage;
import hangman.logismate.entity.ChatRoom;
import hangman.logismate.service.ChatService;
import hangman.logismate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat Controller", description = "웹소켓 기반 실시간 채팅 API")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "채팅방 생성 또는 조회", description = "1:1 채팅방을 생성하거나 기존 채팅방을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 생성 또는 기존 채팅방 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (예: 자기 자신과 채팅 요청)")
    })
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoom> createChatRoom(
            @RequestParam Long otherUserId,
            HttpServletRequest request
    ) {
        Long currentUserId = jwtUtil.getUserIdFromRequest(request);
        if (currentUserId == null || currentUserId.equals(otherUserId)) {
            return ResponseEntity.badRequest().build();
        }
        ChatRoom chatRoom = chatService.getOrCreateChatRoom(currentUserId, otherUserId);
        return ResponseEntity.ok(chatRoom);
    }

    @Operation(summary = "로그인한 사용자의 채팅방 목록 조회", description = "현재 로그인한 사용자가 참여한 모든 채팅방을 조회합니다. 각 채팅방의 마지막 메시지 및 전송 시간을 포함합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (예: 사용자 정보 없음)")
    })
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms(HttpServletRequest request) {
        Long currentUserId = jwtUtil.getUserIdFromRequest(request);
        if (currentUserId == null) {
            return ResponseEntity.badRequest().build();
        }
        List<ChatRoomResponse> rooms = chatService.getChatRoomsForUser(currentUserId);
        return ResponseEntity.ok(rooms);
    }

    @Operation(summary = "특정 채팅방의 메시지 조회", description = "채팅방 ID를 기반으로 전체 채팅 메시지를 조회합니다. 오래된 메시지 순으로 정렬됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채팅 메시지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (예: 존재하지 않는 채팅방)")
    })
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessage>> getChatMessages(
            @PathVariable Long chatRoomId,
            HttpServletRequest request
    ) {
        // 로그인한 사용자가 해당 채팅방의 참여자인지 추가 검증 가능 (생략)
        List<ChatMessage> messages = chatService.getChatMessages(chatRoomId);
        return ResponseEntity.ok(messages);
    }
}
