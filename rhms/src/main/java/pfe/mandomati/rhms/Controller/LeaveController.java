package pfe.mandomati.rhms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.LeaveDto;
import pfe.mandomati.rhms.service.LeaveService;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody LeaveDto leaveDto) {
        return leaveService.register(leaveDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteLeave(@PathVariable Long id) {
        return leaveService.deleteLeave(id);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateLeave(@PathVariable Long id, @RequestBody LeaveDto leaveDto) {
        return leaveService.updateLeave(id, leaveDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

}
