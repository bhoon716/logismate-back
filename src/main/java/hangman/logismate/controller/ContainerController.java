package hangman.logismate.controller;

import hangman.logismate.dto.ContainerRegisterRequest;
import hangman.logismate.dto.ContainerRegisterResponse;
import hangman.logismate.dto.ContainerSearchResponse;
import hangman.logismate.enums.AdditionalService;
import hangman.logismate.enums.ImportExport;
import hangman.logismate.enums.InsuranceType;
import hangman.logismate.enums.Region;
import hangman.logismate.service.ContainerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/container")
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    // 화주: 컨테이너 검색 (GET 요청으로 변경)
    @GetMapping("/search")
    public ResponseEntity<List<ContainerSearchResponse>> searchContainer(
            @RequestParam(required = false) ImportExport importExport,
            @RequestParam(required = false) Region departure,
            @RequestParam(required = false) Region destination,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) Double volume,
            @RequestParam(required = false) LocalDate expectedArrivalDate,
            @RequestParam(required = false) Set<InsuranceType> insuranceTypes,
            @RequestParam(required = false) Set<AdditionalService> additionalServices,
            @RequestParam(required = false) Double minimumCost,
            @RequestParam(required = false) Double maximumCost
    ) {
        List<ContainerSearchResponse> response = containerService.searchContainer(
                importExport, departure, destination, weight, volume, expectedArrivalDate,
                insuranceTypes, additionalServices, minimumCost, maximumCost
        );
        return ResponseEntity.ok(response);
    }

    // 포워더: 컨테이너 등록
    @PostMapping("/register")
    public ResponseEntity<ContainerRegisterResponse> registerContainer(
            @RequestBody ContainerRegisterRequest request, HttpServletRequest httpRequest
    ) {
        ContainerRegisterResponse response = containerService.registerContainer(request, httpRequest);
        return ResponseEntity.ok(response);
    }
}
