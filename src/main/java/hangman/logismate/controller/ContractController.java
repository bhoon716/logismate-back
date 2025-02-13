package hangman.logismate.controller;

import hangman.logismate.dto.ContractRequest;
import hangman.logismate.dto.ContractResponse;
import hangman.logismate.enums.ContractStatus;
import hangman.logismate.service.ContractService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/request")
    public ResponseEntity<ContractResponse> request(@RequestBody ContractRequest request, HttpServletRequest httpRequest) {
        ContractResponse response = contractService.requestContract(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shipper")
    public ResponseEntity<List<ContractResponse>> getShipperContracts(HttpServletRequest httpRequest) {
        List<ContractResponse> response = contractService.getShipperContracts(httpRequest);
        return ResponseEntity.ok(response);
    }

    // 계약의 상태 별로 조회
    @GetMapping("/status")
    public ResponseEntity<List<ContractResponse>> getContractsByStatus(
            HttpServletRequest httpRequest,
            @RequestParam ContractStatus contractStatus) {

        List<ContractResponse> response = contractService.getAllContractByStatus(contractStatus, httpRequest);
        return ResponseEntity.ok(response);
    }

    // 특정 계약 조회
    @GetMapping("/{id}")
    ResponseEntity<ContractResponse> getContract(@PathVariable Long id, HttpServletRequest httpRequest) {
        ContractResponse response = contractService.getContract(id, httpRequest);
        return ResponseEntity.ok(response);
    }

    // 계약 상태 변경
    @PutMapping("/{id}")
    public ResponseEntity<ContractResponse> changeContractStatus(
            @PathVariable Long id,
            @RequestParam ContractStatus status) {
        ContractResponse response = contractService.setContractStatus(id, status);
        return ResponseEntity.ok(response);
    }
}
