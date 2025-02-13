package hangman.logismate.controller;

import hangman.logismate.dto.ContainerRegisterRequest;
import hangman.logismate.dto.ContainerRegisterResponse;
import hangman.logismate.dto.ContainerSearchRequest;
import hangman.logismate.dto.ContainerSearchResponse;
import hangman.logismate.service.ContainerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/container")
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    // 화주: 컨테이너 검색
    @PostMapping("/search")
    public ResponseEntity<List<ContainerSearchResponse>> searchContainer(@RequestBody ContainerSearchRequest request){
        List<ContainerSearchResponse> response = containerService.searchContainer(request);
        return ResponseEntity.ok(response);
    }

    // 포워더: 컨테이너 등록
    @PostMapping("/register")
    public ResponseEntity<ContainerRegisterResponse> registerContainer(@RequestBody ContainerRegisterRequest request, HttpServletRequest httpRequest){
        ContainerRegisterResponse response = containerService.registerContainer(request, httpRequest);
        return ResponseEntity.ok(response);
    }

    // 화주: 컨테이너 개별 조회
    @GetMapping("/{containerId}")
    public ResponseEntity<ContainerSearchResponse> getContainer(@PathVariable Long containerId){
        ContainerSearchResponse response = containerService.getContainer(containerId);
        return ResponseEntity.ok(response);
    }
}
