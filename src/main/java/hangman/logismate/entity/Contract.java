package hangman.logismate.entity;

import hangman.logismate.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "forwarder_id", nullable = false)
    private User forwarder; // 포워더 (계약을 제공하는 사용자)

    @ManyToOne
    @JoinColumn(name = "shipper_id", nullable = false)
    private User shipper; // 화주 (계약을 요청하는 사용자)

    @ManyToOne
    @JoinColumn(name = "container_id", nullable = false)
    private Container container; // 계약된 컨테이너

    @Column(nullable = false)
    private LocalDate contractDate; // 계약 체결 날짜

    @Column(nullable = false)
    private ImportExport importExport; // 수출, 수입

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ElementCollection
    private Set<InsuranceType> insuranceTypes; // 보험 종류

    @Enumerated(EnumType.STRING)
    @ElementCollection
    @Column(nullable = false)
    private Set<AdditionalService> additionalServices; // 추가 서비스

    @Column(nullable = false)
    private Double cost; // 총 운송 비용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus contractStatus; // 계약 상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CargoStatus cargoStatus; // 화물 상태 (선적 준비 완료, 항해중, 입항 및 통관, 도착지 운송 중, 운송 완료)
}

