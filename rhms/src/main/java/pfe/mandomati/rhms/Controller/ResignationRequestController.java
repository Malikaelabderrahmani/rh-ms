package pfe.mandomati.rhms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.ResignationRequestDto;
import pfe.mandomati.rhms.service.ResignationRequestService;

@RestController
@RequestMapping("/resignationrequest")
@RequiredArgsConstructor
public class ResignationRequestController {

    private final ResignationRequestService resignationRequestService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> requestResignation(@RequestBody ResignationRequestDto resignationRequestDto) {
        return resignationRequestService.requestResignation(resignationRequestDto);
    }

    @PostMapping("/cancel/{requestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> cancelResignationRequest(@PathVariable Long requestId) {
        return resignationRequestService.cancelResignationRequest(requestId);
    }

    @GetMapping("/requestsbycni/{cni}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> getResignationRequestsByCni(@PathVariable String cni) {
        return resignationRequestService.getResignationRequestsByCni(cni);
    }

    @PostMapping("/process/{requestId}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> processResignationRequest(@PathVariable Long requestId, @RequestParam boolean isApproved) {
        return resignationRequestService.processResignationRequest(requestId, isApproved);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllResignationRequests() {
        return resignationRequestService.getAllResignationRequests();
    }

    

}
