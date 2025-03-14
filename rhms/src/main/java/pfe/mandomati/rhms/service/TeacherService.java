package pfe.mandomati.rhms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.TeacherDto;

public interface TeacherService {

    ResponseEntity<String> register(TeacherDto teacherDto, Long id);
    List<TeacherDto> getTeacherEmployees();

}
