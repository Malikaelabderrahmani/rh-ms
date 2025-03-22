package pfe.mandomati.rhms.service;

import java.util.List;

import pfe.mandomati.rhms.Dto.AdminDto;
import pfe.mandomati.rhms.Dto.AdminD;

import org.springframework.http.ResponseEntity;


public interface AdminService {

    ResponseEntity<String> register(AdminDto adminDto);
    List<AdminD> getAdminEmployees();
    ResponseEntity<String> deleteAdmin(Long adminId, String username);
    ResponseEntity<String> updateAdmin(Long adminId, String username, AdminDto adminDto);
    ResponseEntity<?> getAdminById(Long id);
}
