package pfe.mandomati.rhms.service;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.LeaveDto;

public interface LeaveService {

    ResponseEntity<String> register(LeaveDto leaveDto);
    ResponseEntity<String> deleteLeave(Long id);
    ResponseEntity<String> updateLeave(Long id, LeaveDto leaveDto);
    ResponseEntity<?> getAllLeaves();


    
}
