package hangman.logismate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AisResponse {
    private double latitude;       // 위도
    private double longitude;      // 경도
    private LocalDateTime etaUtc;  // 도착 예정 시간 (LocalDateTime)
}
