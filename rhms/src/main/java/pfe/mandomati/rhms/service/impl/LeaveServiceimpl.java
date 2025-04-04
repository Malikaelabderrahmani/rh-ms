package pfe.mandomati.rhms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.Dto.LeaveDto;
import pfe.mandomati.rhms.model.Employee;
import pfe.mandomati.rhms.model.Leave;
import pfe.mandomati.rhms.repository.LeaveRepository;
import pfe.mandomati.rhms.service.LeaveService;

@Service
@RequiredArgsConstructor
public class LeaveServiceimpl implements LeaveService {

    private final LeaveRepository leaveRepository;
    private static final Logger log = LoggerFactory.getLogger(LeaveServiceimpl.class);

     @Override
     @Transactional
     @PreAuthorize("hasRole('RH')")
     public ResponseEntity<String> register(LeaveDto leaveDto) {
         try {
             // Vérifier si un congé avec le même CNI, startDate et endDate existe déjà
             boolean leaveExists = leaveRepository.existsByEmployeeCniAndStartDateAndEndDate(
                 leaveDto.getEmployeeCni(), leaveDto.getStartDate(), leaveDto.getEndDate()
             );
     
             if (leaveExists) {
                 return ResponseEntity.status(HttpStatus.CONFLICT).body("Leave already exists");
             }
     
             saveUserLocally(leaveDto); // Enregistrer uniquement dans la base de données
             return ResponseEntity.ok("Leave registered successfully");
         } catch (Exception e) {
             log.error("Registration failed for leave: {}", leaveDto.getId(), e);
             throw new RuntimeException("Failed to register leave", e);
         }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteLeave(Long id) {
        try {
            leaveRepository.deleteById(id);
            return ResponseEntity.ok("Leave deleted successfully");
        } catch (Exception e) {
            log.error("Deletion failed for leave: {}", id, e);
            throw new RuntimeException("Failed to delete leave", e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateLeave(Long id, LeaveDto leaveDto) {
        try {
            Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave not found"));
            leave.setEmployeeCni(leaveDto.getEmployeeCni());
            leave.setEmployeefName(leaveDto.getEmployeefName());
            leave.setEmployeelName(leaveDto.getEmployeelName());
            leave.setEndDate(leaveDto.getEndDate());
            leave.setStartDate(leaveDto.getStartDate());
            leave.setStatus(leaveDto.getStatus());
            leave.setType(leaveDto.getType());
            leaveRepository.save(leave);
            return ResponseEntity.ok("Leave updated successfully");
        } catch (Exception e) {
            log.error("Update failed for leave: {}", id, e);
            throw new RuntimeException("Failed to update leave", e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllLeaves() {
        try {
            return ResponseEntity.ok(leaveRepository.findAll());
        } catch (Exception e) {
            log.error("Failed to retrieve leaves", e);
            throw new RuntimeException("Failed to retrieve leaves", e);
        }
    }
     

    private void saveUserLocally(LeaveDto leaveDto) {
        Leave leave = new Leave();
        leave.setEmployeeCni(leaveDto.getEmployeeCni());
        leave.setEmployeefName(leaveDto.getEmployeefName());
        leave.setEmployeelName(leaveDto.getEmployeelName());
        leave.setEndDate(leaveDto.getEndDate());
        leave.setStartDate(leaveDto.getStartDate());
        leave.setStatus(leaveDto.getStatus());
        leave.setType(leaveDto.getType());
        leaveRepository.save(leave);
    }


}
