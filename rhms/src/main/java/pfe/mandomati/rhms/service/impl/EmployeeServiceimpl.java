package pfe.mandomati.rhms.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.enums.Job;
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
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(EmployeeDto employeeDto) {
        // Vérifier si l'employé existe déjà avec CNI ou CNSS Number
        boolean exists = employeeRepository.existsByCniOrCnssNumber(employeeDto.getCni(), employeeDto.getCnssNumber());
        
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Employee already exists with the same CNI or CNSS number");
        }

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
    @PreAuthorize("hasRole('RH')")
    public List<EmployeeDto> getEmployeesByJob(Job job) {
        List<Employee> employees = employeeRepository.findByJob(job);
        return employees.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }
        
        employeeRepository.deleteById(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateEmployee(Long id, EmployeeDto employeeDto) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        Employee employee = optionalEmployee.get();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setTelephone(employeeDto.getTelephone());
        employee.setAdress(employeeDto.getAdress());
        employee.setHireDate(employeeDto.getHireDate());
        employee.setCnssNumber(employeeDto.getCnssNumber());
        employee.setJob(employeeDto.getJob());

        employeeRepository.save(employee);
        return ResponseEntity.ok("Employee updated successfully");
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
        }

        Employee employee = optionalEmployee.get();
        EmployeeDto employeeDto = mapToDto(employee);
        return ResponseEntity.ok(employeeDto);
    }

    private EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
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