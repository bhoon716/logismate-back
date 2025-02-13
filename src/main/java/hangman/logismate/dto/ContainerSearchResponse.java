package hangman.logismate.dto;

import hangman.logismate.entity.Container;
import hangman.logismate.entity.User;
import hangman.logismate.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ContainerSearchResponse {

    private Long id;
    private User forwarder;
    private ImportExport importExport;
    private Region departure;
    private Region destination;
    private LocalDate expectedArrivalDate;
    private Set<InsuranceType> insuranceTypes;
    private Set<AdditionalService> additionalServices;
    private Double maxWeight;
    private Double maxVolume;
    private ContractStatus contractStatus;
    private Double cost;

    public static ContainerSearchResponse fromEntity(Container container) {
        return ContainerSearchResponse.builder()
                .id(container.getId())
                .forwarder(container.getForwarder())
                .importExport(container.getImportExport())
                .departure(container.getDeparture())
                .destination(container.getDestination())
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
