package pfe.mandomati.rhms.service;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import pfe.mandomati.rhms.Dto.UserDto;
import pfe.mandomati.rhms.model.Employee;
import pfe.mandomati.rhms.repository.RHRepository;

public class RHService {

    RHRepository rhRepository;

    // @Override
    // @Transactional
    // public ResponseEntity<String> register(UserDto userDTO) {
    //     try {
    //         ResponseEntity<String> response = keycloakService.registerUser(userDTO);
    //         if (response.getStatusCode().is2xxSuccessful()) {
    //             saveUserLocally(userDTO);
    //         }
    //         return response;
    //     } catch (Exception e) {
    //         log.error("Registration failed for user: {}", userDTO.getUsername(), e);
    //         throw new RuntimeException("Failed to register user", e);
    //     }
    // }
    //     private void saveUserLocally(UserDto userDTO) {
    //     Employee employee = new Employee();
    //     employee.setCni(userDTO.getCni());
    //     employee.setHireDate(userDTO.getHireDate());
    //     employee.setCnssNumber(userDTO.getCnssNumber());
    //     rhRepository.save(employee);
    // }
}
