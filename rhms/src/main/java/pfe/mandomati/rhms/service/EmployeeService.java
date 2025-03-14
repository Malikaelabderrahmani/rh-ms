package pfe.mandomati.rhms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.enums.Job;


public interface EmployeeService {

    ResponseEntity<String> register(EmployeeDto employeeDTO);
    List<EmployeeDto> getEmployeesByJob(Job job);
    ResponseEntity<String> deleteEmployee(Long id);
    ResponseEntity<String> updateEmployee(Long id, EmployeeDto employeeDto);
}
