package hangman.logismate.dto;

import hangman.logismate.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class ContainerRegisterRequest {

    private ImportExport importExport;
    private Region departure;
    private Region destination;
    private LocalDate expectedDepartureDate;
    private LocalDate expectedArrivalDate;
    private Set<InsuranceType> insuranceTypes;
    private Set<AdditionalService> additionalServices;
    private Double maxWeight;
    private Double maxVolume;
    private Double cost;
}
