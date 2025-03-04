package pfe.mandomati.rhms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.EmployeeUserDto;

public interface EmployeeUserService {

    ResponseEntity<String> register(EmployeeUserDto employeeuserDTO, Long id, String role);
    List<EmployeeUserDto> getAllEmployeeUsers();

}
