package hangman.logismate.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hangman.logismate.dto.AisResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class AisService {

    // 🔹 application.properties 또는 application.yml 에서 API Key를 주입
    @Value("${api.datalastic.key}")
    private String apiKey;

    // 🔹 API 기본 URL
    private static final String BASE_API_URL = "https://api.datalastic.com/api/v0/vessel_pro";

    // 🔹 RestTemplate & JSON Parser
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME; // "2025-02-13T21:00:00Z" 형식 대응

    public AisResponse getAisData(String mmsi) {
        try {
            if (apiKey == null || apiKey.isBlank()) {
                throw new RuntimeException("❌ API Key가 설정되지 않았습니다.");
            }

            // API URL 동적 생성
            String apiUrl = String.format("%s?api-key=%s&mmsi=%s", BASE_API_URL, apiKey, mmsi);

            System.out.println("🔎 API 요청 URL: " + apiUrl); // 요청 확인 로그

            // API 호출
            String jsonResponse = restTemplate.getForObject(apiUrl, String.class);

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                throw new RuntimeException("❌ API 응답이 없습니다.");
            }

            // JSON 파싱
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode meta = root.path("meta");

            // 🔹 API Key가 유효하지 않은 경우 처리
            if (!meta.path("success").asBoolean()) {
                throw new RuntimeException("❌ API Key가 유효하지 않거나 만료되었습니다.");
            }

            JsonNode dataNode = root.path("data");

            // 위도, 경도, ETA 추출
            double latitude = dataNode.path("lat").asDouble(Double.NaN);
            double longitude = dataNode.path("lon").asDouble(Double.NaN);
            String etaString = dataNode.path("eta_UTC").asText("");

            if (Double.isNaN(latitude) || Double.isNaN(longitude) || etaString.isEmpty()) {
                throw new RuntimeException("❌ API 응답 데이터가 유효하지 않습니다.");
            }

            // ETA 문자열을 LocalDateTime으로 변환
            LocalDateTime etaUtc = LocalDateTime.parse(etaString, DateTimeFormatter.ISO_DATE_TIME);

            return new AisResponse(latitude, longitude, etaUtc);

        } catch (Exception e) {
            throw new RuntimeException("❌ API 호출 또는 JSON 파싱 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
