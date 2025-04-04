package pfe.mandomati.rhms.service;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.LeaveRequestDto;

public interface LeaveRequestService {

    ResponseEntity<String> requestLeave(LeaveRequestDto leaveRequestDto);
    ResponseEntity<String> processLeaveRequest(Long requestId, boolean isApproved);
    ResponseEntity<String> cancelLeaveRequest(Long requestId);
    ResponseEntity<?> getAllLeaveRequests();
}
