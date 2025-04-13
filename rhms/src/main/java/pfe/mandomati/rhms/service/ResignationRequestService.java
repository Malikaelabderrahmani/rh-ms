package pfe.mandomati.rhms.service;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.ResignationRequestDto;

public interface ResignationRequestService {
    ResponseEntity<String> requestResignation(ResignationRequestDto requestDto);
    ResponseEntity<String> cancelResignationRequest(Long requestId);
    ResponseEntity<?> getResignationRequestsByCni(String cni);
    ResponseEntity<String> processResignationRequest(Long requestId, boolean isApproved);
    ResponseEntity<?> getAllResignationRequests();

    
}
