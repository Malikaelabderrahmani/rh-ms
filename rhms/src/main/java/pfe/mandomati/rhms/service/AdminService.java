package pfe.mandomati.rhms.service;

import java.util.List;

import pfe.mandomati.rhms.Dto.AdminDto;
import org.springframework.http.ResponseEntity;


public interface AdminService {

    ResponseEntity<String> register(AdminDto adminDto);
    List<AdminDto> getAdminEmployees();
    ResponseEntity<String> deleteAdmin(Long adminId, String username);
    ResponseEntity<String> updateAdmin(Long adminId, String username, AdminDto adminDto);

}
