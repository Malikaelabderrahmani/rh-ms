package pfe.mandomati.rhms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.TeacherD;
import pfe.mandomati.rhms.Dto.TeacherDto;

public interface TeacherService {

    ResponseEntity<String> register(TeacherDto teacherDto);
    List<TeacherD> getTeacherEmployees();
    public ResponseEntity<String> updateTeacher(Long teacherId, String username, TeacherDto teacherDto);
    public ResponseEntity<String> deleteTeacher(Long teacherId, String username);

}
