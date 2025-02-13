package hangman.logismate.dto;

import hangman.logismate.entity.User;
import hangman.logismate.enums.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ContractRequest {

    private Long containerId; // 계약할 컨테이너 정보

    private ImportExport importExport; // 수출, 수입

    private Region departure; // 출발지

    private Region destination; // 도착지

    private Double weight; // 계약할 화물 용량 (kg)

    private Double volume; // 계약할 화물 부피 (cbm)

    private Set<InsuranceType> insuranceTypes; // 보험 종류

    private Set<AdditionalService> additionalServices; // 추가 서비스

    private Double cost; // 운임 비용
}
