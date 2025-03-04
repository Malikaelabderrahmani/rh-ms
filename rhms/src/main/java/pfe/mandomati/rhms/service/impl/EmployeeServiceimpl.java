package pfe.mandomati.rhms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.model.Employee;
import pfe.mandomati.rhms.repository.EmployeeRepository;
import pfe.mandomati.rhms.service.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceimpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceimpl.class);
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public ResponseEntity<String> register(EmployeeDto employeeDto) {
        try {
            saveUserLocally(employeeDto); // Enregistrer uniquement dans la base de données
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("Registration failed for user: {}", employeeDto.getCni(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }
    
    private void saveUserLocally(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setTelephone(employeeDto.getTelephone());
        employee.setAdress(employeeDto.getAdress());
        employee.setCni(employeeDto.getCni());
        employee.setHireDate(employeeDto.getHireDate());
        employee.setCnssNumber(employeeDto.getCnssNumber());
        employee.setJob(employeeDto.getJob());
        employeeRepository.save(employee);
    }

    
    @Override
    @Transactional
    public List<EmployeeDto> getEmployeesByJob(String job) {
        List<Employee> employees = employeeRepository.findByJob(job);
        return employees.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .cni(employee.getCni())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .telephone(employee.getTelephone())
                .adress(employee.getAdress())
                .hireDate(employee.getHireDate())
                .cnssNumber(employee.getCnssNumber())
                .job(employee.getJob())
                .build();
    }
}
