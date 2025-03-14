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
import pfe.mandomati.rhms.Dto.AdminDto;
import pfe.mandomati.rhms.Dto.UserDto;
import pfe.mandomati.rhms.model.Admin;
import pfe.mandomati.rhms.repository.AdminRepository;
import pfe.mandomati.rhms.repository.UserClient;
import pfe.mandomati.rhms.service.AdminService;

@Service
@RequiredArgsConstructor
public class AdminServiceimpl implements AdminService {

    private final UserClient userClient;
    private final AdminRepository adminRepository;
    private static final Logger log = LoggerFactory.getLogger(AdminServiceimpl.class);

    /**
     * Récupérer tous les utilisateurs.
     */
    public List<UserDto> fetchAllUsers() {
        return userClient.getAllUsers();
    }

    /**
     * Enregistrer un administrateur.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(AdminDto adminDto, Long id) {
        // Vérifier si l'admin existe déjà avec userId, CNI ou CNSS Number
        boolean exists = adminRepository.existsByUserIdOrCniOrCnssNumber(id, adminDto.getCni(), adminDto.getCnssNumber());
    
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Admin already exists with the same userId, CNI, or CNSS number");
        }

        try {
            saveUserLocally(adminDto, id);
            return ResponseEntity.ok("Admin registered successfully");
        } catch (Exception e) {
            log.error("Registration failed for admin: {}", adminDto.getCni(), e);
            throw new RuntimeException("Failed to register admin", e);
        }
    }


    /**
     * Sauvegarde un administrateur localement en BDD.
     */
    private void saveUserLocally(AdminDto adminDto, Long id) {
        Admin admin = new Admin();
        admin.setUserId(id);
        admin.setCni(adminDto.getCni());
        admin.setHireDate(adminDto.getHireDate());
        admin.setCnssNumber(adminDto.getCnssNumber());
        admin.setPosition(adminDto.getPosition());
        adminRepository.save(admin);
    }

    /**
     * Récupérer tous les administrateurs en faisant un match avec `UserDto`.
     */
    @Override
    @PreAuthorize("hasRole('RH')")
    public List<AdminDto> getAdminEmployees() {
        List<UserDto> adminUsers = userClient.getAdmins(); // Récupérer les admins depuis IAM-MS
        List<Admin> admins = adminRepository.findAll(); // Récupérer les admins de la BDD RH-MS

        // Création d'une map (userId -> UserDto) pour accès rapide
        Map<Long, UserDto> userMap = adminUsers.stream()
                .collect(Collectors.toMap(UserDto::getId, user -> user));

        return admins.stream()
                .map(admin -> mapToDto(admin, userMap.get(admin.getUserId()))) // Mapper les admins avec les users
                .collect(Collectors.toList());
    }

    /**
     * Convertit un Admin + UserDto en AdminDto.
     */
    private AdminDto mapToDto(Admin admin, UserDto userDto) {
        return AdminDto.builder()
                .cni(admin.getCni())
                .hireDate(admin.getHireDate())
                .cnssNumber(admin.getCnssNumber())
                .position(admin.getPosition())
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
