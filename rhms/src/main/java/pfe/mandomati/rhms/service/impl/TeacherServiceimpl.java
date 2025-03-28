package pfe.mandomati.rhms.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONObject;
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

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;

// import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.RoleDto;
import pfe.mandomati.rhms.Dto.TeacherD;
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
    public List<TeacherD> getTeacherEmployees() {
        List<TeacherD> teacherUsers = userClient.getTeachers();
        List<Teacher> teachers = teacherRepository.findAll();

        Map<Long, TeacherD> userMap = teacherUsers.stream()
                .collect(Collectors.toMap(TeacherD::getId, user -> user));

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

    @Override
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public ResponseEntity<?> getTeacherById(Long id) {
    // 1️ Vérifier si le professeur existe en base locale
    Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
    if (optionalTeacher.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found");
    }

    Teacher teacher = optionalTeacher.get();

    try {
        // 2️ Récupérer les informations utilisateur depuis IAM-MS
        String url = "https://iamms.mandomati.com/api/auth/user/get/" + id;
        ResponseEntity<TeacherD> response = restTemplate.exchange(url, HttpMethod.GET, null, TeacherD.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return ResponseEntity.ok(mapToDto(teacher, null)); // Retourner uniquement les infos locales
        }

        TeacherD teacherD = response.getBody();

        // 3️ Mapper et retourner les données fusionnées
        return ResponseEntity.ok(mapToDto(teacher, teacherD));

    } catch (Exception e) {
        log.error("Error occurred while fetching teacher by ID", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing request");
    }
}

@Override
@PreAuthorize("hasAnyRole('RH', 'ADMIN')")
public ResponseEntity<?> getTeachersBySpeciality(String speciality) {
    List<Teacher> teachers = teacherRepository.findBySpeciality(speciality);
    if (teachers.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No teachers found for this speciality");
    }

    // Récupérer les informations des enseignants depuis IAM-MS
    List<TeacherD> teacherUsers = userClient.getTeachers();
    Map<Long, TeacherD> userMap = teacherUsers.stream()
            .collect(Collectors.toMap(TeacherD::getId, user -> user));

    // Mapper et retourner les données fusionnées
    List<TeacherD> result = teachers.stream()
            .map(teacher -> mapToD(teacher, userMap.get(teacher.getUserId())))
            .collect(Collectors.toList());

    return ResponseEntity.ok(result);
}

    @Override
    @PreAuthorize("hasAnyRole('RH', 'ADMIN')")
    public List<TeacherD> getTeachers() {
        List<TeacherD> teacherUsers = userClient.getTeachers();
        List<Teacher> teachers = teacherRepository.findAll();

        Map<Long, TeacherD> userMap = teacherUsers.stream()
            .collect(Collectors.toMap(TeacherD::getId, user -> user));

        return teachers.stream()
            .map(teacher -> mapToD(teacher, userMap.get(teacher.getUserId())))
            .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> getTeacherFromToken(String token) {
        try {
            // Extraire le username depuis le token JWT
            String username = extractUsernameFromToken(token);
    
            // Récupérer l'ID de l'utilisateur depuis IAM-MS
            String url = "https://iamms.mandomati.com/api/auth/user/profile/" + username;
            ResponseEntity<TeacherD> response = restTemplate.exchange(url, HttpMethod.GET, null, TeacherD.class);
    
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found in IAM-MS");
            }
    
            TeacherD iamUserResponse = response.getBody();
            Long teacherId = iamUserResponse.getId(); // Suppose que IAM-MS vous renvoie un champ "id" pour l'enseignant
    
            // Récupérer l'enseignant par son ID depuis la base de données locale
            Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
    
            if (optionalTeacher.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found in local database");
            }
    
            Teacher teacher = optionalTeacher.get();
    
            // Fusionner les données locales et IAM-MS si nécessaire
            return ResponseEntity.ok(mapToDto(teacher, iamUserResponse));
    
        } catch (Exception e) {
            log.error("Error occurred while fetching teacher from token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing request");
        }
    }
    
    public String extractUsernameFromToken(String token) {
        try {
            // Séparer le token en 3 parties : Header, Payload, Signature
            String payload = token.split("\\.")[1];

            // Décoder le payload en base64
            String decodedPayload = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);

            // Extraire l'username du payload (en supposant que l'username soit sous la clé "preferred_username")
            JSONObject json = new JSONObject(decodedPayload);
            return json.getString("preferred_username");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token format or unable to extract username", e);
        }
    }


    /**
     * Convertit un prof + UserDto en profDto.
     */
    private TeacherD mapToDto(Teacher teacher, TeacherD teacherD) {
        return TeacherD.builder()
                .cni(teacher.getCni())
                .hireDate(teacher.getHireDate())
                .cnssNumber(teacher.getCnssNumber())
                .speciality(teacher.getSpeciality())
                .id(teacherD != null ? teacherD.getId() : null)
                .lastname(teacherD != null ? teacherD.getLastname() : null)
                .firstname(teacherD != null ? teacherD.getFirstname() : null)
                .email(teacherD != null ? teacherD.getEmail() : null)
                .address(teacherD != null ? teacherD.getAddress() : null)
                .birthDate(teacherD != null ? teacherD.getBirthDate() : null)
                .city(teacherD != null ? teacherD.getCity() : null)
                //.role(teacherD != null ? teacherD.getRole() : null)
                .build();
    }
    
    private TeacherD mapToD(Teacher teacher, TeacherD teacherD) {
        return TeacherD.builder()
                .speciality(teacher.getSpeciality())
                .id(teacherD != null ? teacherD.getId() : null)
                .lastname(teacherD != null ? teacherD.getLastname() : null)
                .firstname(teacherD != null ? teacherD.getFirstname() : null)
                .email(teacherD != null ? teacherD.getEmail() : null)
                //.role(teacherD != null ? teacherD.getRole() : null)
                .build();
    }
}
