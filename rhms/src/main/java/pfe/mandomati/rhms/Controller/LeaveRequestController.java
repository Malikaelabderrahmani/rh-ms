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
import pfe.mandomati.rhms.Dto.LeaveRequestDto;
import pfe.mandomati.rhms.service.LeaveRequestService;

@RestController
@RequestMapping("/requestleaves")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> requestLeave(@RequestBody LeaveRequestDto leaveRequestDto) {
        return leaveRequestService.requestLeave(leaveRequestDto);
    }

    @PostMapping("/process/{requestId}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> processLeaveRequest(@PathVariable Long requestId, @RequestParam boolean isApproved) {
        return leaveRequestService.processLeaveRequest(requestId, isApproved);
    }

    @PostMapping("/cancel/{requestId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<String> cancelLeaveRequest(@PathVariable Long requestId) {
        return leaveRequestService.cancelLeaveRequest(requestId);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllLeaveRequests() {
        return leaveRequestService.getAllLeaveRequests();

    }

}
