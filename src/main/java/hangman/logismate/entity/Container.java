package hangman.logismate.entity;

import hangman.logismate.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "containers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Container {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User forwarder; // 포워더 정보

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportExport importExport; // 수출/수입 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region departure; // 출발지

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Region destination; // 도착지

    @Column(nullable = false)
    private LocalDate expectedDepartureDate; // 출발 예정일

    @Column(nullable = false)
    private LocalDate expectedArrivalDate; // 도착 예정일

    @ElementCollection(targetClass = InsuranceType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "container_insurances", joinColumns = @JoinColumn(name = "container_id"))
    @Column(name = "insurance_type")
    private Set<InsuranceType> insuranceTypes; // 보험 종류

    @ElementCollection(targetClass = AdditionalService.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "container_additional_services", joinColumns = @JoinColumn(name = "container_id"))
    @Column(name = "additional_service")
    private Set<AdditionalService> additionalServices; // 추가 서비스

    @Column(nullable = false)
    private Double maxWeight; // 최대 무게 (kg)

    @Column(nullable = false)
    private Double maxVolume; // 최대 부피 (cbm)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus contractStatus; // 계약 상태 (예: 대기, 진행 중, 완료)

    @Column(nullable = false)
    private Double cost; // 운임 비용
}
