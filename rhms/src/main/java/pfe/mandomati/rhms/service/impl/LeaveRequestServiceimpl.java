package pfe.mandomati.rhms.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pfe.mandomati.rhms.Dto.LeaveRequestDto;
import pfe.mandomati.rhms.model.Leave;
import pfe.mandomati.rhms.model.LeaveRequest;
import pfe.mandomati.rhms.repository.LeaveRepository;
import pfe.mandomati.rhms.repository.LeaveRequestRepository;
import pfe.mandomati.rhms.service.LeaveRequestService;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceimpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveRepository leaveRepository;
    private static final Logger log = LoggerFactory.getLogger(LeaveRequestServiceimpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> requestLeave(LeaveRequestDto leaveRequestDto) {
        try {
             // Vérifier si un congé avec le même CNI, startDate et endDate existe déjà
             boolean requestleaveExists = leaveRequestRepository.existsByEmployeeCniAndStartDateAndEndDate(
                 leaveRequestDto.getEmployeeCni(), leaveRequestDto.getStartDate(), leaveRequestDto.getEndDate()
             );
     
             if (requestleaveExists) {
                 return ResponseEntity.status(HttpStatus.CONFLICT).body("Leave request already exists");
             }
     
             saveUserLocally(leaveRequestDto); // Enregistrer uniquement dans la base de données
             return ResponseEntity.ok("Leave request registered successfully");
         } catch (Exception e) {
             log.error("Registration failed for leave request: {}", leaveRequestDto.getId(), e);
             throw new RuntimeException("Failed to register leave request", e);
         }
    }
     

    private void saveUserLocally(LeaveRequestDto leaverequestDto) {
        LeaveRequest leaverequest = new LeaveRequest();
        leaverequest.setEmployeeCni(leaverequestDto.getEmployeeCni());
        leaverequest.setEmployeefName(leaverequestDto.getEmployeefName());
        leaverequest.setEmployeelName(leaverequestDto.getEmployeelName());
        leaverequest.setEndDate(leaverequestDto.getEndDate());
        leaverequest.setStartDate(leaverequestDto.getStartDate());
        leaverequest.setStatus(leaverequestDto.getStatus());
        leaverequest.setType(leaverequestDto.getType());
        leaveRequestRepository.save(leaverequest);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> processLeaveRequest(Long requestId, boolean isApproved) {
        LeaveRequest request = leaveRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave request not found");
        }

        if (isApproved) {
            Leave leave = new Leave();
            leave.setEmployeeCni(request.getEmployeeCni());
            leave.setEmployeefName(request.getEmployeefName());
            leave.setEmployeelName(request.getEmployeelName());
            leave.setStartDate(request.getStartDate());
            leave.setEndDate(request.getEndDate());
            leave.setType(request.getType());
            leave.setStatus("APPROVED");
            leaveRepository.save(leave);
        }

        leaveRequestRepository.delete(request);
        return ResponseEntity.ok(isApproved ? "Leave approved and recorded" : "Leave request rejected and deleted");
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> cancelLeaveRequest(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId).orElse(null);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Leave request not found");
        }
    
        leaveRequestRepository.delete(request);
        return ResponseEntity.ok("Leave request cancelled successfully");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllLeaveRequests() {
        try {
            return ResponseEntity.ok(leaveRequestRepository.findAll());
        } catch (Exception e) {
            log.error("Failed to retrieve leave requests", e);
            throw new RuntimeException("Failed to retrieve leave requests", e);
        }
    }

}
