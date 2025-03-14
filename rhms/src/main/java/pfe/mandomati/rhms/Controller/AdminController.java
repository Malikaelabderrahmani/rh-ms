package pfe.mandomati.rhms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.AdminDto;
import pfe.mandomati.rhms.service.AdminService;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/register/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody AdminDto adminDto, @PathVariable Long id) {
        return adminService.register(adminDto, id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public List<AdminDto> getAllAdmins() {
        return adminService.getAdminEmployees();
    }
}
