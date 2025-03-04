package pfe.mandomati.rhms.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeDto;
import pfe.mandomati.rhms.repository.EmployeeRepository;
import pfe.mandomati.rhms.service.EmployeeService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody EmployeeDto employeeDTO) {
        return employeeService.register(employeeDTO);
    }

    @GetMapping("all/{job}")
    public List<EmployeeDto> getEmployeesByJob(@PathVariable String job) {
        return employeeService.getEmployeesByJob(job);
    }

    
}
