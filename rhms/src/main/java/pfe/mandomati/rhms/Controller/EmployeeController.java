package pfe.mandomati.rhms.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.enums.Job;
import pfe.mandomati.rhms.service.EmployeeService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    
}
