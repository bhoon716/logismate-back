package hangman.logismate.dto;

import hangman.logismate.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ContainerSearchRequest {

    private ImportExport importExport; // 수출/수입 여부
    private TransportMethod transportMethod; // 해상, 항공, 특송
    private Region departure; // 출발지
    private Region destination; // 도착지
    private Double weight; // 검색 시 필터링할 최대 용량 (kg)
    private Double volume; // 검색 시 필터링할 최대 부피 (cbm)
    private LocalDate expectedArrivalDate; // 도착 희망일
    private Set<InsuranceType> insuranceTypes; // 보험 종류
    private Set<AdditionalService> additionalServices; // 추가 서비스
    private Double minimumCost; // 최소 운임 비용
    private Double maximumCost; // 최대 운임 비용
}
