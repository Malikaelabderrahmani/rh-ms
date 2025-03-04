package pfe.mandomati.rhms.service.impl;

import pfe.mandomati.rhms.Dto.EmployeeUserDto;
import pfe.mandomati.rhms.Dto.UserDto;
import pfe.mandomati.rhms.model.EmployeeUser;
import pfe.mandomati.rhms.repository.EmployeeUserRepository;
import pfe.mandomati.rhms.repository.UserClient;
import pfe.mandomati.rhms.service.EmployeeUserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeUserServiceimpl implements EmployeeUserService {

    private final UserClient userClient;
    private static final Logger log = LoggerFactory.getLogger(EmployeeUserServiceimpl.class);
    private final EmployeeUserRepository employeeuserRepository;

    public List<UserDto> fetchAllUsers() {
        return userClient.getAllUsers();
    }

    @Override
    @Transactional
    public ResponseEntity<String> register(EmployeeUserDto employeeuserDto, Long id) {
        try {
            saveUserLocally(employeeuserDto, id); // Enregistrer uniquement dans la base de données
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            log.error("Registration failed for user: {}", employeeuserDto.getCni(), e);
            throw new RuntimeException("Failed to register user", e);
        }
    }
    
    private void saveUserLocally(EmployeeUserDto employeeuserDto, Long id) {
        EmployeeUser employeeuser = new EmployeeUser();
        employeeuser.setUserId(id);
        employeeuser.setCni(employeeuserDto.getCni());
        employeeuser.setHireDate(employeeuserDto.getHireDate());
        employeeuser.setCnssNumber(employeeuserDto.getCnssNumber());
        employeeuserRepository.save(employeeuser);
    }

    @Override
    public List<EmployeeUserDto> getAdminEmployees() {
        return getEmployeeUsersByRole(userClient.getAdmins()); 
    }
    @Override
    public List<EmployeeUserDto> getTeacherEmployees() {
        return getEmployeeUsersByRole(userClient.getTeachers());
    }

    private List<EmployeeUserDto> getEmployeeUsersByRole(List<UserDto> users) {
        List<EmployeeUser> employeeUsers = employeeuserRepository.findAll(); // Récupération des employés
    
        // Création d'une map (userId -> UserDto) pour accès rapide
        Map<Long, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(UserDto::getId, user -> user));
    
        return employeeUsers.stream()
                .filter(employeeUser -> userMap.containsKey(employeeUser.getUserId())) // Vérifier si user_id est présent
                .map(employeeUser -> mapToDto(employeeUser, userMap.get(employeeUser.getUserId()))) // Mapper vers DTO
                .collect(Collectors.toList());
    }

     public List<EmployeeUserDto> getAllEmployeeUsers() {
        List<UserDto> users = userClient.getAllUsers(); // Récupération des utilisateurs depuis IAM-MS
        List<EmployeeUser> employeeUsers = employeeuserRepository.findAll(); // Récupération des employés
        
        // Création d'une map (userId -> UserDto) pour accès rapide
        Map<Long, UserDto> userMap = users.stream()
                .collect(Collectors.toMap(UserDto::getId, user -> user));

        return employeeUsers.stream()
                .map(employeeUser -> mapToDto(employeeUser, userMap.get(employeeUser.getUserId())))
                .collect(Collectors.toList());
    }

    /**
     * Convertit un EmployeeUser + UserDto en EmployeeUserDto.
     */
    private EmployeeUserDto mapToDto(EmployeeUser employeeUser, UserDto userDto) {
        return EmployeeUserDto.builder()
                .cni(employeeUser.getCni())
                .hireDate(employeeUser.getHireDate())
                .cnssNumber(employeeUser.getCnssNumber())
                .lastname(userDto != null ? userDto.getLastname() : null)
                .firstname(userDto != null ? userDto.getFirstname() : null)
                .email(userDto != null ? userDto.getEmail() : null)
                .address(userDto != null ? userDto.getAddress() : null)
                .birthDate(userDto != null ? userDto.getBirthDate() : null)
                .city(userDto != null ? userDto.getCity() : null)
                .build();
    }



}
