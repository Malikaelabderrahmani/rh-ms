package pfe.mandomati.rhms.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.RoleDto;
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
    private final RestTemplate restTemplate = new RestTemplate();

    // @Override
    // public List<UserDto> fetchAllUsers() {
    //     return userClient.getAllUsers();
    // }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(TeacherDto teacherDto) {
        if (teacherRepository.existsByUserIdOrCniOrCnssNumber(teacherDto.getId(), teacherDto.getCni(), teacherDto.getCnssNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher already exists");
        }

        try {
            RoleDto roleDto = teacherDto.getRole() != null ? teacherDto.getRole() : RoleDto.builder().name("TEACHER").build();
            
            UserDto userDto = UserDto.builder()
                    .username(teacherDto.getUsername())
                    .lastname(teacherDto.getLastname())
                    .firstname(teacherDto.getFirstname())
                    .email(teacherDto.getEmail())
                    .password(teacherDto.getPassword())
                    .address(teacherDto.getAddress())
                    .birthDate(teacherDto.getBirthDate())
                    .city(teacherDto.getCity())
                    .role(roleDto)
                    .build();

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://iamms.mandomati.com/api/auth/register", userDto, String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to register teacher in IAM-MS");
            }

            Long id = extractIdFromResponse(response.getBody());
            saveUserLocally(teacherDto, id);
            return ResponseEntity.ok("Teacher registered successfully");

        } catch (Exception e) {
            log.error("Registration failed for teacher: {}", teacherDto.getCni(), e);
            throw new RuntimeException("Failed to register teacher", e);
        }
    }

    private Long extractIdFromResponse(String responseBody) {
        Pattern pattern = Pattern.compile("ID: (\\d+)");
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        } else {
            throw new RuntimeException("Failed to extract ID from response: " + responseBody);
        }
    }

    private void saveUserLocally(TeacherDto teacherDto, Long id) {
        Teacher teacher = new Teacher();
        teacher.setUserId(id);
        teacher.setCni(teacherDto.getCni());
        teacher.setHireDate(teacherDto.getHireDate());
        teacher.setCnssNumber(teacherDto.getCnssNumber());
        teacher.setSpeciality(teacherDto.getSpeciality());
        teacherRepository.save(teacher);
    }

    @Override
    @PreAuthorize("hasRole('RH')")
    public List<TeacherDto> getTeacherEmployees() {
        List<UserDto> teacherUsers = userClient.getTeachers();
        List<Teacher> teachers = teacherRepository.findAll();

        Map<Long, UserDto> userMap = teacherUsers.stream()
                .collect(Collectors.toMap(UserDto::getId, user -> user));

        return teachers.stream()
                .map(teacher -> mapToDto(teacher, userMap.get(teacher.getUserId())))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('RH')")
    @Transactional
    public ResponseEntity<String> deleteTeacher(Long teacherId, String username) {
        try {
            String deleteUrl = "https://iamms.mandomati.com/api/auth/user/delete/" + username;
            ResponseEntity<String> deleteResponse = restTemplate.exchange(
                    deleteUrl, HttpMethod.DELETE, null, String.class
            );

            if (!deleteResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(deleteResponse.getStatusCode()).body("Failed to delete teacher from IAM-MS");
            }

            teacherRepository.deleteById(teacherId);
            return ResponseEntity.ok("Teacher deleted successfully from IAM-MS and local database");
        } catch (Exception e) {
            log.error("Error occurred while deleting teacher", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing request");
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateTeacher(Long teacherId, String username, TeacherDto teacherDto) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
        if (optionalTeacher.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
        }

        Teacher teacher = optionalTeacher.get();

        try {
            UserDto userDto = UserDto.builder()
                    .username(username)
                    .lastname(teacherDto.getLastname())
                    .firstname(teacherDto.getFirstname())
                    .email(teacherDto.getEmail())
                    .address(teacherDto.getAddress())
                    .birthDate(teacherDto.getBirthDate())
                    .city(teacherDto.getCity())
                    .role(teacherDto.getRole())
                    .build();

            String editUrl = "https://iamms.mandomati.com/api/auth/user/edit/" + username;
            ResponseEntity<String> editResponse = restTemplate.exchange(
                    editUrl, HttpMethod.PUT, new HttpEntity<>(userDto), String.class
            );

            if (!editResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(editResponse.getStatusCode()).body("Failed to update teacher in IAM-MS");
            }

            teacher.setCni(teacherDto.getCni());
            teacher.setHireDate(teacherDto.getHireDate());
            teacher.setCnssNumber(teacherDto.getCnssNumber());
            teacher.setSpeciality(teacherDto.getSpeciality());
            teacherRepository.save(teacher);

            return ResponseEntity.ok("Teacher updated successfully");
        } catch (Exception e) {
            log.error("Error occurred while updating teacher", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing request");
        }
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
