package hangman.logismate.dto;

import hangman.logismate.entity.Contract;
import hangman.logismate.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ContractResponse {

    private Long contractId; // 계약 ID
    private Long forwarderId; // 포워더 ID
    private Long shipperId; // 화주 ID
    private Long containerId; // 계약된 컨테이너 ID
    private LocalDate contractDate; // 계약 체결 날짜
    private ImportExport importExport; // 수출, 수입 여부
    private Set<InsuranceType> insuranceTypes; // 보험 종류
    private Set<AdditionalService> additionalServices; // 추가 서비스
    private Double cost; // 총 운송 비용
    private ContractStatus contractStatus; // 계약 상태
    private CargoStatus cargoStatus; // 화물 상태

    /**
     * Contract 엔티티를 ContractResponse DTO로 변환하는 메서드
     */
    public static ContractResponse fromEntity(Contract contract) {
        return ContractResponse.builder()
                .contractId(contract.getId())
                .forwarderId(contract.getForwarder().getId())
                .shipperId(contract.getShipper().getId())
                .containerId(contract.getContainer().getId())
                .contractDate(contract.getContractDate())
                .importExport(contract.getImportExport())
                .insuranceTypes(contract.getInsuranceTypes())
                .additionalServices(contract.getAdditionalServices())
                .cost(contract.getCost())
                .contractStatus(contract.getContractStatus())
                .cargoStatus(contract.getCargoStatus())
                .build();
    }
}
