package pfe.mandomati.rhms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.TeacherD;
import pfe.mandomati.rhms.Dto.TeacherDto;
import pfe.mandomati.rhms.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody TeacherDto teacherDto) {
        return teacherService.register(teacherDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public List<TeacherD> getAllTeachers() {
        return teacherService.getTeacherEmployees();
    }

    @GetMapping("/allteachers")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public List<TeacherD> getAllTeachersDs() {
        return teacherService.getTeachers();
    }

    @DeleteMapping("delete/{id}/{username}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteTeacher(@PathVariable Long id, @PathVariable String username) {
        return teacherService.deleteTeacher(id, username);
    }

    @PutMapping("edit/{id}/{username}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateTeacher(@PathVariable Long id, @PathVariable String username, @RequestBody TeacherDto teacherDto) {
        return teacherService.updateTeacher(id, username, teacherDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        return teacherService.getTeacherById(id);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getTeacherFromToken(@RequestHeader("Authorization") String authorizationHeader) {
            // Extraire le token "Bearer <token>" et récupérer le token
            String token = authorizationHeader.replace("Bearer ", "");

            // Appeler le service avec le token extrait
            return teacherService.getTeacherFromToken(token);
    }


    @GetMapping("/speciality/{speciality}")
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<?> getTeachersBySpeciality(@PathVariable String speciality) {
        return teacherService.getTeachersBySpeciality(speciality);
    }

}
