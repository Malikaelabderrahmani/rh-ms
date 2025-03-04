package pfe.mandomati.rhms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.EmployeeUserDto;
import pfe.mandomati.rhms.Dto.UserDto;
import pfe.mandomati.rhms.service.EmployeeUserService;
import pfe.mandomati.rhms.service.impl.EmployeeUserServiceimpl;

@RestController
@RequestMapping("/employeeuser")
@RequiredArgsConstructor
public class EmployeeUserController {

    //private final EmployeeUserServiceimpl employeeUserServiceimpl;
    private final EmployeeUserService employeeuserService;

    @PostMapping("/register/{id}")
    public ResponseEntity<String> register(@RequestBody EmployeeUserDto employeeuserDTO, @PathVariable Long id) {
        return employeeuserService.register(employeeuserDTO, id);
    }

    // @GetMapping("/all")
    // public List<UserDto> getAllUsers() {
    //     return employeeUserServiceimpl.fetchAllUsers();
    // }
    @GetMapping("/all")
    public List<EmployeeUserDto> getAllEmployeeUsers() {
        return employeeuserService.getAllEmployeeUsers();
    }

    @GetMapping("/admins")
    public ResponseEntity<List<EmployeeUserDto>> getAdminEmployees() {
        List<EmployeeUserDto> admins = employeeuserService.getAdminEmployees();
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<EmployeeUserDto>> getTeacherEmployees() {
        List<EmployeeUserDto> teachers = employeeuserService.getTeacherEmployees();
        return ResponseEntity.ok(teachers);
    }
}
