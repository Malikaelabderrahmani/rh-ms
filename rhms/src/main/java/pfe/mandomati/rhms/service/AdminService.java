package pfe.mandomati.rhms.service;

import java.util.List;

import pfe.mandomati.rhms.Dto.AdminDto;
import org.springframework.http.ResponseEntity;


public interface AdminService {

    ResponseEntity<String> register(AdminDto adminDto, Long id);
    List<AdminDto> getAdminEmployees();

}
