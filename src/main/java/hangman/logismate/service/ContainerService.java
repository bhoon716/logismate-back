package hangman.logismate.service;

import hangman.logismate.dto.ContainerRegisterRequest;
import hangman.logismate.dto.ContainerRegisterResponse;
import hangman.logismate.dto.ContainerSearchRequest;
import hangman.logismate.dto.ContainerSearchResponse;
import hangman.logismate.entity.Container;
import hangman.logismate.entity.User;
import hangman.logismate.enums.ContractStatus;
import hangman.logismate.enums.UserRole;
import hangman.logismate.repository.ContainerRepository;
import hangman.logismate.repository.UserRepository;
import hangman.logismate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContainerService {

    private final ContainerRepository containerRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 화주: 컨테이너 검색
    public List<ContainerSearchResponse> searchContainer(ContainerSearchRequest request) {
        return containerRepository.findAll().stream()
                .filter(container -> request.getImportExport() == null || container.getImportExport().equals(request.getImportExport()))
                .filter(container -> request.getTransportMethod() == null || container.getTransportMethod().equals(request.getTransportMethod()))
                .filter(container -> request.getDeparture() == null || container.getDeparture().equals(request.getDeparture()))
                .filter(container -> request.getDestination() == null || container.getDestination().equals(request.getDestination()))
                .filter(container -> request.getExpectedArrivalDate() == null || container.getExpectedArrivalDate().isBefore(request.getExpectedArrivalDate()) || container.getExpectedArrivalDate().equals(request.getExpectedArrivalDate())) // 🔹 도착 희망일 이전 또는 같은 날짜 필터링
                .filter(container -> request.getWeight() == null || container.getMaxWeight() >= request.getWeight())  // 최대 무게 필터링
                .filter(container -> request.getVolume() == null || container.getMaxVolume() >= request.getVolume())  // 최대 부피 필터링
                .filter(container -> request.getMinimumCost() == null || container.getCost() >= request.getMinimumCost())  // 최소 비용 필터링
                .filter(container -> request.getMaximumCost() == null || container.getCost() <= request.getMaximumCost())  // 최대 비용 필터링
                .filter(container -> request.getInsuranceTypes() == null || request.getInsuranceTypes().isEmpty() || container.getInsuranceTypes().containsAll(request.getInsuranceTypes()))  // 보험 필터링
                .filter(container -> request.getAdditionalServices() == null || request.getAdditionalServices().isEmpty() || container.getAdditionalServices().containsAll(request.getAdditionalServices()))  // 추가 서비스 필터링
                .map(ContainerSearchResponse::fromEntity)
                .toList();
    }

    // 포워더: 컨테이너 등록
    public ContainerRegisterResponse registerContainer(ContainerRegisterRequest request, HttpServletRequest httpRequest) {
        Long forwarderId = jwtUtil.getUserIdFromRequest(httpRequest);
        User forwarder = userRepository.findById(forwarderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포워더"));
        if (!forwarder.getUserRole().equals(UserRole.FORWARDER)) {
            throw new IllegalArgumentException("존재하지 않는 포워더");
        }

        Container container = Container.builder()
                .forwarder(forwarder)
                .importExport(request.getImportExport())
                .transportMethod(request.getTransportMethod())
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
}
