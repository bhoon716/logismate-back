package hangman.logismate.controller;

import hangman.logismate.dto.AisResponse;
import hangman.logismate.dto.ContractRequest;
import hangman.logismate.dto.ContractResponse;
import hangman.logismate.enums.ContractStatus;
import hangman.logismate.service.AisService;
import hangman.logismate.service.ContractService;
import hangman.logismate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final AisService aisService;

    // 계약 요청
    @PostMapping()
    public ResponseEntity<ContractResponse> requestContract(@RequestBody ContractRequest request, HttpServletRequest httpRequest){
        ContractResponse response = contractService.requestContract(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    // 특정 계약의 AIS 정보 조회
    @GetMapping("/ais/{contractId}")
    public ResponseEntity<AisResponse> getAis(@PathVariable Long contractId) {
        ContractResponse contract = contractService.getContract(contractId);
        String mmsi = contract.getMmsi();
        AisResponse aisResponse = aisService.getAisData(mmsi);
        return ResponseEntity.ok(aisResponse);
    }

    // 로그인한 유저의 모든 계약 조회
    @GetMapping("/my-contracts")
    public ResponseEntity<List<ContractResponse>> getMyContracts(HttpServletRequest request) {
        List<ContractResponse> contracts = contractService.getAllShipperContract(request);
        return ResponseEntity.ok(contracts);
    }

    // 특정 계약 정보 조회
    @GetMapping("/{contractId}")
    public ResponseEntity<ContractResponse> getContract(@PathVariable Long contractId) {
        ContractResponse contract = contractService.getContract(contractId);
        return ResponseEntity.ok(contract);
    }

    // 포워더: 계약 상태별 조회
    @GetMapping("/status/{contractStatus}")
    public ResponseEntity<List<ContractResponse>> getContractsByStatus(@PathVariable ContractStatus contractStatus, HttpServletRequest request) {
        List<ContractResponse> contracts = contractService.getAllContractByStatus(contractStatus, request);
        return ResponseEntity.ok(contracts);
    }

    // 포워더: 계약 상태 변경 (수락 또는 거절)
    @PutMapping("/{contractId}/status")
    public ResponseEntity<ContractResponse> updateContractStatus(@PathVariable Long contractId, @RequestParam ContractStatus contractStatus) {
        ContractResponse updatedContract = contractService.setContractStatus(contractId, contractStatus);
        return ResponseEntity.ok(updatedContract);
    }

}
