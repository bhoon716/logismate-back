package hangman.logismate.service;

import hangman.logismate.dto.ContainerRegisterRequest;
import hangman.logismate.dto.ContainerRegisterResponse;
import hangman.logismate.dto.ContainerSearchResponse;
import hangman.logismate.entity.Container;
import hangman.logismate.entity.User;
import hangman.logismate.enums.*;
import hangman.logismate.repository.ContainerRepository;
import hangman.logismate.repository.UserRepository;
import hangman.logismate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 화주: 컨테이너 검색
    public List<ContainerSearchResponse> searchContainer(
            ImportExport importExport, Region departure, Region destination, Double weight,
            Double volume, LocalDate expectedArrivalDate, Set<InsuranceType> insuranceTypes,
            Set<AdditionalService> additionalServices, Double minimumCost, Double maximumCost
    ) {
        return containerRepository.findAll().stream()
                .filter(container -> importExport == null || container.getImportExport() == importExport)
                .filter(container -> departure == null || container.getDeparture() == departure)
                .filter(container -> destination == null || container.getDestination() == destination)
                .filter(container -> expectedArrivalDate == null || !container.getExpectedArrivalDate().isAfter(expectedArrivalDate))
                .filter(container -> weight == null || container.getMaxWeight() >= weight)
                .filter(container -> volume == null || container.getMaxVolume() >= volume)
                .filter(container -> minimumCost == null || container.getCost() >= minimumCost)
                .filter(container -> maximumCost == null || container.getCost() <= maximumCost)
                .filter(container -> insuranceTypes == null || insuranceTypes.isEmpty() ||
                        container.getInsuranceTypes().stream().anyMatch(insuranceTypes::contains))
                .filter(container -> additionalServices == null || additionalServices.isEmpty() ||
                        container.getAdditionalServices().stream().anyMatch(additionalServices::contains))
                .map(ContainerSearchResponse::fromEntity)
                .toList();
    }

    // 포워더: 컨테이너 등록
    public ContainerRegisterResponse registerContainer(ContainerRegisterRequest request, HttpServletRequest httpRequest) {
        Long forwarderId = jwtUtil.getUserIdFromRequest(httpRequest);
        User forwarder = userRepository.findById(forwarderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포워더"));

        if (forwarder.getUserRole() != UserRole.FORWARDER) {
            throw new IllegalArgumentException("포워더 권한이 없습니다.");
        }

        Container container = Container.builder()
                .forwarder(forwarder)
                .importExport(request.getImportExport())
                .departure(request.getDeparture())
                .destination(request.getDestination())
                .expectedDepartureDate(request.getExpectedDepartureDate())
                .expectedArrivalDate(request.getExpectedArrivalDate())
                .insuranceTypes(request.getInsuranceTypes())
                .additionalServices(request.getAdditionalServices())
                .maxWeight(request.getMaxWeight())
                .maxVolume(request.getMaxVolume())
                .contractStatus(ContractStatus.REQUESTED) // 기본 상태를 '대기'로 설정
                .cost(request.getCost())
                .build();

        Container saved = containerRepository.save(container);
        return ContainerRegisterResponse.fromEntity(saved);
    }

    // AIS 선박 위치 추적
}
