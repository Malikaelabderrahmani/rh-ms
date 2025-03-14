package pfe.mandomati.rhms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.TeacherDto;
import pfe.mandomati.rhms.Dto.UserDto;
import pfe.mandomati.rhms.model.Teacher;
import pfe.mandomati.rhms.repository.TeacherRepository;
import pfe.mandomati.rhms.repository.UserClient;
import pfe.mandomati.rhms.service.TeacherService;

@Service
@RequiredArgsConstructor
public class TeacherServiceimpl implements TeacherService {

    private final UserClient userClient;
    private final TeacherRepository teacherRepository;
    private static final Logger log = LoggerFactory.getLogger(TeacherServiceimpl.class);

    /**
     * Récupérer tous les utilisateurs.
     */
    public List<UserDto> fetchAllUsers() {
        return userClient.getAllUsers();
    }

    /**
     * Enregistrer un prof.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(TeacherDto teacherDto, Long id) {
        if (teacherRepository.existsByUserIdOrCniOrCnssNumber(id, teacherDto.getCni(), teacherDto.getCnssNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher already exists with the same userId, CNI, or CNSS number");
        }

        try {
            saveUserLocally(teacherDto, id);
            return ResponseEntity.ok("Teacher registered successfully");
        } catch (Exception e) {
            log.error("Registration failed for teacher: {}", teacherDto.getCni(), e);
            throw new RuntimeException("Failed to register teacher", e);
        }
    }


    /**
     * Sauvegarde un prof localement en BDD.
     */
    private void saveUserLocally(TeacherDto teacherDto, Long id) {
        Teacher teacher = new Teacher();
        teacher.setUserId(id);
        teacher.setCni(teacherDto.getCni());
        teacher.setHireDate(teacherDto.getHireDate());
        teacher.setCnssNumber(teacherDto.getCnssNumber());
        teacher.setSpeciality(teacherDto.getSpeciality());
        teacherRepository.save(teacher);
    }

    /**
     * Récupérer tous les profs en faisant un match avec `UserDto`.
     */
    @Override
    @PreAuthorize("hasRole('RH')")
    public List<TeacherDto> getTeacherEmployees() {
        List<UserDto> teacherUsers = userClient.getTeachers(); // Récupérer les profs depuis IAM-MS
        List<Teacher> teachers = teacherRepository.findAll(); // Récupérer les prof de la BDD RH-MS

        // Création d'une map (userId -> UserDto) pour accès rapide
        Map<Long, UserDto> userMap = teacherUsers.stream()
                .collect(Collectors.toMap(UserDto::getId, user -> user));

        return teachers.stream()
                .map(teacher -> mapToDto(teacher, userMap.get(teacher.getUserId()))) // Mapper les profs avec les users
                .collect(Collectors.toList());
    }

    /**
     * Convertit un prof + UserDto en profDto.
     */
    private TeacherDto mapToDto(Teacher teacher, UserDto userDto) {
        return TeacherDto.builder()
                .cni(teacher.getCni())
                .hireDate(teacher.getHireDate())
                .cnssNumber(teacher.getCnssNumber())
                .speciality(teacher.getSpeciality())
                .lastname(userDto != null ? userDto.getLastname() : null)
                .firstname(userDto != null ? userDto.getFirstname() : null)
                .email(userDto != null ? userDto.getEmail() : null)
                .address(userDto != null ? userDto.getAddress() : null)
                .birthDate(userDto != null ? userDto.getBirthDate() : null)
                .city(userDto != null ? userDto.getCity() : null)
                .role(userDto != null ? userDto.getRole() : null)
                .build();
    }
}
