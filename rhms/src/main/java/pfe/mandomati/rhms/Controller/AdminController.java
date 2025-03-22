package pfe.mandomati.rhms.controller;

import java.util.List;

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
import pfe.mandomati.rhms.Dto.AdminDto;
import pfe.mandomati.rhms.Dto.AdminD;
import pfe.mandomati.rhms.service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody AdminDto adminDto) {
        return adminService.register(adminDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public List<AdminD> getAllAdmins() {
        return adminService.getAdminEmployees();
    }

    @DeleteMapping("delete/{id}/{username}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id, @PathVariable String username) {
        return adminService.deleteAdmin(id, username);
    }

    @PutMapping("edit/{id}/{username}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id, @PathVariable String username, @RequestBody AdminDto adminDto) {
        return adminService.updateAdmin(id, username, adminDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id);
    }
}
