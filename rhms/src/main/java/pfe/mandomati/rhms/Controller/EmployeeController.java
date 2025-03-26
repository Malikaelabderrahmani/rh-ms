package pfe.mandomati.rhms.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.enums.Job;
import pfe.mandomati.rhms.service.EmployeeService;

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


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody EmployeeDto employeeDTO) {
        return employeeService.register(employeeDTO);
    }

    @GetMapping("all/{job}")
    @PreAuthorize("hasRole('RH')")
    public List<EmployeeDto> getEmployeesByJob(@PathVariable Job job) {
        return employeeService.getEmployeesByJob(job);
    }

    @DeleteMapping("delete/{id}")  //  Suppression d'un employé
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployee(id);
    }

    @PutMapping("edit/{id}")  //  Mise à jour d'un employé
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        return employeeService.updateEmployee(id, employeeDto);
    }

    @GetMapping("/{id}")  //  Récupération d'un employé par son id
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    
}
