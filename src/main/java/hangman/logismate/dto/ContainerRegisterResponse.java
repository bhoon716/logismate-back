package hangman.logismate.dto;

import hangman.logismate.entity.Container;
import hangman.logismate.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ContainerRegisterResponse {

    private Long id;
    private Long forwarderId;
    private ImportExport importExport;
    private Region departure;
    private Region destination;
    private LocalDate expectedDepartureDate;
    private LocalDate expectedArrivalDate;
    private Set<InsuranceType> insuranceTypes;
    private Set<AdditionalService> additionalServices;
    private Double maxWeight;
    private Double maxVolume;
    private ContractStatus contractStatus;
    private Double cost;

    public static ContainerRegisterResponse fromEntity(Container container) {
        return ContainerRegisterResponse.builder()
                .id(container.getId())
                .forwarderId(container.getForwarder().getId())
                .importExport(container.getImportExport())
                .departure(container.getDeparture())
                .destination(container.getDestination())
                .expectedDepartureDate(container.getExpectedDepartureDate())
                .expectedArrivalDate(container.getExpectedArrivalDate())
                .insuranceTypes(container.getInsuranceTypes())
                .additionalServices(container.getAdditionalServices())
                .maxWeight(container.getMaxWeight())
                .maxVolume(container.getMaxVolume())
                .contractStatus(container.getContractStatus())
                .cost(container.getCost())
                .build();
    }
}
