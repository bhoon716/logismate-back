package hangman.logismate.service;

import hangman.logismate.dto.ContractRequest;
import hangman.logismate.dto.ContractResponse;
import hangman.logismate.entity.Container;
import hangman.logismate.entity.Contract;
import hangman.logismate.entity.User;
import hangman.logismate.enums.CargoStatus;
import hangman.logismate.enums.ContractStatus;
import hangman.logismate.enums.UserRole;
import hangman.logismate.repository.ContainerRepository;
import hangman.logismate.repository.ContractRepository;
import hangman.logismate.repository.UserRepository;
import hangman.logismate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContainerRepository containerRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // 화주: 계약 요청
    public ContractResponse requestContract(ContractRequest request, HttpServletRequest httpRequest) {
        Container container = containerRepository.findById(request.getContainerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 컨테이너"));

        Long shipperId = jwtUtil.getUserIdFromRequest(httpRequest);

        User shipper = userRepository.findById(shipperId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 화주"));
        if (!shipper.getUserRole().equals(UserRole.SHIPPER)) {
            throw new IllegalArgumentException("존재하지 않는 화주");
        }

        Contract contract = Contract.builder()
                .forwarder(container.getForwarder())
                .shipper(shipper)
                .container(container)
                .contractDate(LocalDate.now())
                .importExport(request.getImportExport())
                .insuranceTypes(request.getInsuranceTypes())
                .additionalServices(request.getAdditionalServices())
                .cost(request.getCost())
                .contractStatus(ContractStatus.REQUESTED)
                .cargoStatus(CargoStatus.LOADING_PREPARED)
                .build();

        Contract save = contractRepository.save(contract);
        return ContractResponse.fromEntity(save);
    }

    // 화주: 본인 계약 모두 조회
    public List<ContractResponse> getShipperContracts(HttpServletRequest httpRequest) {
        Long shipperId = jwtUtil.getUserIdFromRequest(httpRequest);

        User shipper = userRepository.findById(shipperId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 화주"));
        if (!shipper.getUserRole().equals(UserRole.SHIPPER)) {
            throw new IllegalArgumentException("존재하지 않는 화주");
        }

        List<Contract> contracts = contractRepository.findAllByShipper(shipper);
        return contracts.stream()
                .map(ContractResponse::fromEntity)
                .toList();
    }

    // 포워더: 계약 상태 별 조회
    public List<ContractResponse> getAllContractByStatus(ContractStatus contractStatus, HttpServletRequest httpRequest) {
        Long forwarderId = jwtUtil.getUserIdFromRequest(httpRequest);
        User forwarder = userRepository.findById(forwarderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 포워더"));
        List<Contract> contracts = contractRepository.findAllByForwarderAndContractStatus(forwarder, contractStatus);
        return contracts.stream()
                .map(ContractResponse::fromEntity)
                .toList();
    }

    // 포워더: 계약 상태 수락 or 거절
    public ContractResponse setContractStatus(Long contractId, ContractStatus contractStatus) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계약"));

        contract.setContractStatus(contractStatus);
        Contract save = contractRepository.save(contract);
        return ContractResponse.fromEntity(save);
    }

    public ContractResponse getContract(Long id, HttpServletRequest httpRequest) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계약"));
        Long userId = jwtUtil.getUserIdFromRequest(httpRequest);
        if(!contract.getShipper().getId().equals(userId) || !contract.getForwarder().getId().equals(userId)){
            throw new IllegalArgumentException("권한 없음");
        }
        return ContractResponse.fromEntity(contract);
    }
}
