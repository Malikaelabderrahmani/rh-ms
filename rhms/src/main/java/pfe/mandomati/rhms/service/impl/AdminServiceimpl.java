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
//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.AdminDto;
import pfe.mandomati.rhms.Dto.RoleDto;
import pfe.mandomati.rhms.Dto.UserDto;
import pfe.mandomati.rhms.Dto.AdminD;
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
    private final RestTemplate restTemplate = new RestTemplate();

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
    public ResponseEntity<String> register(AdminDto adminDto) {
        boolean exists = adminRepository.existsByUserIdOrCniOrCnssNumber(adminDto.getId(), adminDto.getCni(), adminDto.getCnssNumber());
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Admin already exists");
        }

        try {
        // Si le rôle est null, on le définit par défaut (ex: "ADMIN")
            RoleDto roleDto = adminDto.getRole() != null ? adminDto.getRole() : RoleDto.builder().name("ADMIN").build();

        // Construire UserDto
            UserDto userDto = UserDto.builder()
                .username(adminDto.getUsername())
                .lastname(adminDto.getLastname())
                .firstname(adminDto.getFirstname())
                .email(adminDto.getEmail())
                .password(adminDto.getPassword())
                .status(adminDto.isStatus())
                .address(adminDto.getAddress())
                .birthDate(adminDto.getBirthDate())
                .city(adminDto.getCity())
                .role(roleDto)
                .createdAt(adminDto.getCreatedAt())
                .build();

        // Envoyer à IAM-MS
            ResponseEntity<String> response = restTemplate.postForEntity(
                "https://iamms.mandomati.com/api/auth/register", userDto, String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to register user in IAM-MS");
            }

      // Récupérer l'ID de l'utilisateur
            Long id = extractIdFromResponse(response.getBody());

            // Sauvegarde locale
            saveUserLocally(adminDto, id);
            return ResponseEntity.ok("Admin registered successfully");

        } catch (Exception e) {
            log.error("Registration failed for admin: {}", adminDto.getCni(), e);
            throw new RuntimeException("Failed to register admin", e);
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
    public List<AdminD> getAdminEmployees() {
        List<AdminD> adminUsers = userClient.getAdmins(); // Récupérer les admins depuis IAM-MS
        List<Admin> admins = adminRepository.findAll(); // Récupérer les admins de la BDD RH-MS

        // Création d'une map (userId -> UserDto) pour accès rapide
        Map<Long, AdminD> userMap = adminUsers.stream()
                .collect(Collectors.toMap(AdminD::getId, user -> user));

        return admins.stream()
                .map(admin -> mapToDto(admin, userMap.get(admin.getUserId()))) // Mapper les admins avec les users
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('RH')")
    @Transactional
    public ResponseEntity<String> deleteAdmin(Long adminId, String username) {
        try {
            // 1. Supprimer l'utilisateur de IAM-MS
            String deleteUrl = "https://iamms.mandomati.com/api/auth/user/delete/" + username;
            ResponseEntity<String> deleteResponse = restTemplate.exchange(
                deleteUrl, HttpMethod.DELETE, null, String.class
            );

            if (!deleteResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(deleteResponse.getStatusCode()).body("Failed to delete user from IAM-MS");
            }

            log.info("User {} deleted successfully from IAM-MS", username);

            // 2. Supprimer l'utilisateur de la base de données locale
            adminRepository.deleteById(adminId);
            log.info("Admin with ID {} deleted from local database", adminId);

            return ResponseEntity.ok("Admin deleted successfully from IAM-MS and local database");

        } catch (Exception e) {
            log.error("Error occurred while deleting admin", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while processing request");
        }
    }


    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateAdmin(Long adminId, String username, AdminDto adminDto) {
        // Vérifier si l'admin existe en base locale
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
    }

    Admin admin = optionalAdmin.get();

    try {
        // 1️ Construire le UserDto pour IAM-MS
            UserDto userDto = UserDto.builder()
                .username(adminDto.getUsername())
                .lastname(adminDto.getLastname())
                .firstname(adminDto.getFirstname())
                .email(adminDto.getEmail())
                .password(adminDto.getPassword())
                .status(adminDto.isStatus())
                .address(adminDto.getAddress())
                .birthDate(adminDto.getBirthDate())
                .city(adminDto.getCity())
                .role(adminDto.getRole())
                .createdAt(adminDto.getCreatedAt())
                .build();

        // 2️ Mettre à jour IAM-MS
        String editUrl = "https://iamms.mandomati.com/api/auth/user/edit/" + username;
        ResponseEntity<String> editResponse = restTemplate.exchange(
                editUrl, HttpMethod.PUT, new HttpEntity<>(userDto), String.class
        );

        if (!editResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(editResponse.getStatusCode()).body("Failed to update admin in IAM-MS");
        }

        log.info("Admin {} updated successfully in IAM-MS", username);

        // 3️⃣ Mettre à jour la base locale (uniquement les champs spécifiques)
        admin.setCni(adminDto.getCni());
        admin.setHireDate(adminDto.getHireDate());
        admin.setCnssNumber(adminDto.getCnssNumber());
        admin.setPosition(adminDto.getPosition());

        adminRepository.save(admin);
        log.info("Admin with ID {} updated locally", adminId);

        return ResponseEntity.ok("Admin updated successfully in IAM-MS and local database");

    } catch (Exception e) {
        log.error("Error occurred while updating admin", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing request");
    }
}

@Override
@PreAuthorize("hasRole('RH')")
public ResponseEntity<?> getAdminById(Long id) {
    // 1️ Vérifier si l'admin existe en base locale
    Optional<Admin> optionalAdmin = adminRepository.findById(id);
    if (optionalAdmin.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
    }

    Admin admin = optionalAdmin.get();

    try {
        // 2️ Récupérer les informations utilisateur depuis IAM-MS
        String url = "https://iamms.mandomati.com/api/auth/user/get/" + id;
        ResponseEntity<AdminD> response = restTemplate.exchange(url, HttpMethod.GET, null, AdminD.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return ResponseEntity.ok(mapToDto(admin, null)); // Retourner uniquement les infos locales
        }

        AdminD adminD = response.getBody();

        // 3️ Mapper et retourner les données fusionnées
        return ResponseEntity.ok(mapToDto(admin, adminD));

    } catch (Exception e) {
        log.error("Error occurred while fetching admin by ID", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while processing request");
    }
}

    @Override
    public ResponseEntity<?> getAdminFromToken(String token) {
        try {
            // Extraire le username depuis le token JWT
            String username = extractUsernameFromToken(token);
    
            // Récupérer l'ID de l'utilisateur depuis IAM-MS
            String url = "https://iamms.mandomati.com/api/auth/user/profile/" + username;
            ResponseEntity<AdminD> response = restTemplate.exchange(url, HttpMethod.GET, null, AdminD.class);
    
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found in IAM-MS");
            }
    
            AdminD iamUserResponse = response.getBody();
            Long adminId = iamUserResponse.getId(); // Suppose que IAM-MS vous renvoie un champ "id" pour l'enseignant
    
            // Récupérer l'enseignant par son ID depuis la base de données locale
            Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
    
            if (optionalAdmin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found in local database");
            }
    
            Admin admin = optionalAdmin.get();
    
            // Fusionner les données locales et IAM-MS si nécessaire
            return ResponseEntity.ok(mapToDto(admin, iamUserResponse));
    
        } catch (Exception e) {
            log.error("Error occurred while fetching admin from token", e);
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
     * Convertit un Admin + UserDto en AdminDto.
     */
    private AdminD mapToDto(Admin admin, AdminD adminD) {
        return AdminD.builder()
                .cni(admin.getCni())
                .hireDate(admin.getHireDate())
                .cnssNumber(admin.getCnssNumber())
                .position(admin.getPosition())
                .id(adminD != null ? adminD.getId() : null)
                .lastname(adminD != null ? adminD.getLastname() : null)
                .firstname(adminD != null ? adminD.getFirstname() : null)
                .email(adminD != null ? adminD.getEmail() : null)
                .address(adminD != null ? adminD.getAddress() : null)
                .birthDate(adminD != null ? adminD.getBirthDate() : null)
                .city(adminD != null ? adminD.getCity() : null)
                //.role(userD != null ? userD.getRole() : null)
                .build();
    }
}
