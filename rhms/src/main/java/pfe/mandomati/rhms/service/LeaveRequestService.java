package pfe.mandomati.rhms.service;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.LeaveRequestDto;

public interface LeaveRequestService {

    ResponseEntity<String> requestLeave(LeaveRequestDto leaveRequestDto);
    ResponseEntity<String> cancelLeaveRequest(Long requestId);
    ResponseEntity<?> getLeaveRequestsByCni(String cni);
    ResponseEntity<String> processLeaveRequest(Long requestId, boolean isApproved);
    ResponseEntity<?> getAllLeaveRequests();
}
