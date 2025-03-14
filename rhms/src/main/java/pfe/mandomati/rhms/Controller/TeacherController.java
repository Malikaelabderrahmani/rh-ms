package pfe.mandomati.rhms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.TeacherDto;
import pfe.mandomati.rhms.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody TeacherDto teacherDto, @PathVariable Long id) {
        return teacherService.register(teacherDto, id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public List<TeacherDto> getAllTeachers() {
        return teacherService.getTeacherEmployees();
    }

}
