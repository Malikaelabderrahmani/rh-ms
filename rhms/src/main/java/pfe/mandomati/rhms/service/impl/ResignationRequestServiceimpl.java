package pfe.mandomati.rhms.service.impl;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.ResignationRequestDto;
import pfe.mandomati.rhms.model.ResignationRequest;
import pfe.mandomati.rhms.repository.AdminRepository;
import pfe.mandomati.rhms.repository.ResignationRequestRepository;
import pfe.mandomati.rhms.repository.TeacherRepository;
import pfe.mandomati.rhms.service.ResignationRequestService;

@Service
@RequiredArgsConstructor
public class ResignationRequestServiceimpl implements ResignationRequestService {

    private final ResignationRequestRepository resignationRequestRepository;
    private final AdminRepository adminRepository;
    private final TeacherRepository teacherRepository;
    private static final Logger log = LoggerFactory.getLogger(ResignationRequestServiceimpl.class);

    //submit resignation request
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> requestResignation(ResignationRequestDto requestDto) {
        try {
        if (!isValidCni(requestDto.getEmployeeCni())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CNI: No matching user found");
        }

        boolean exists = resignationRequestRepository.existsByEmployeeCni(requestDto.getEmployeeCni());
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Resignation request already exists");
        }

        ResignationRequest request = ResignationRequest.builder()
                .employeeCni(requestDto.getEmployeeCni())
                .employeeFName(requestDto.getEmployeefName())
                .employeeLName(requestDto.getEmployeelName())
                .reason(requestDto.getReason())
                .requestDate(LocalDate.now())
                .status("PENDING")
                .build();
        
        resignationRequestRepository.save(request);
        return ResponseEntity.ok("Resignation request submitted successfully");
        } catch (Exception e) {
        log.error("Failed to submit resignation request: {}", requestDto.getEmployeeCni(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit resignation request");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> cancelResignationRequest(Long requestId) {
        try {
            ResignationRequest request = resignationRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Resignation request not found"));

            resignationRequestRepository.delete(request);
            return ResponseEntity.ok("Resignation request cancelled successfully");
        } catch (Exception e) {
            log.error("Failed to cancel resignation request: {}", requestId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to cancel resignation request");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> getResignationRequestsByCni(String cni) {
        try {
            if (!isValidCni(cni)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CNI: No matching user found");
            }
            return ResponseEntity.ok(resignationRequestRepository.findByEmployeeCni(cni));
        } catch (Exception e) {
            log.error("Failed to retrieve resignation requests for CNI: {}", cni, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve resignation requests");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> processResignationRequest(Long requestId, boolean isApproved) {
        try {
            ResignationRequest request = resignationRequestRepository.findById(requestId)
                    .orElseThrow(() -> new RuntimeException("Resignation request not found"));

            if (isApproved) {
                request.setStatus("APPROVED");
            } else {
                request.setStatus("REJECTED");
            }

            resignationRequestRepository.save(request);
            return ResponseEntity.ok("Resignation request processed successfully");
        } catch (Exception e) {
            log.error("Failed to process resignation request: {}", requestId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process resignation request");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllResignationRequests() {
        try {
            return ResponseEntity.ok(resignationRequestRepository.findAll());
        } catch (Exception e) {
            log.error("Failed to retrieve resignation requests", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve resignation requests");
        }
    }

    private boolean isValidCni(String cni) {
        return adminRepository.existsByCni(cni)
            || teacherRepository.existsByCni(cni);
    }

}
