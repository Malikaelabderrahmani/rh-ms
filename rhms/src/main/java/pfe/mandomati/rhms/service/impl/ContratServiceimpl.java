package pfe.mandomati.rhms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.ContratDto;
import pfe.mandomati.rhms.model.Contrat;
import pfe.mandomati.rhms.repository.ContratRepository;
import pfe.mandomati.rhms.service.ContratService;

@Service
@RequiredArgsConstructor
public class ContratServiceimpl implements ContratService {

    private final ContratRepository contratRepository;
    private static final Logger log = LoggerFactory.getLogger(ContratServiceimpl.class);


    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(ContratDto contratDto) {
        try {
            // Vérifier si un contrat avec le même CNI, startDate et endDate existe déjà
            boolean contratExists = contratRepository.existsByEmployeeCniAndStartDateAndEndDate(
                contratDto.getEmployeeCni(), contratDto.getStartDate(), contratDto.getEndDate()
            );

            if (contratExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Contrat already exists");
            }

            saveUserLocally(contratDto); // Enregistrer uniquement dans la base de données
            return ResponseEntity.ok("Contrat registered successfully");
        } catch (Exception e) {
            log.error("Registration failed for contrat: {}", contratDto.getId(), e);
            throw new RuntimeException("Failed to register contrat", e);
        }

    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteContrat(Long id) {
        try {
            contratRepository.deleteById(id);
            return ResponseEntity.ok("Contrat deleted successfully");
        } catch (Exception e) {
            log.error("Deletion failed for contrat: {}", id, e);
            throw new RuntimeException("Failed to delete contrat", e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateContrat(Long id, ContratDto contratDto) {
        try {
            Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrat not found"));

            // Mettre à jour les champs du contrat
            contrat.setEmployeeCni(contratDto.getEmployeeCni());
            contrat.setEmployeefName(contratDto.getEmployeeFName());
            contrat.setEmployeelName(contratDto.getEmployeeFName());
            contrat.setEndDate(contratDto.getEndDate());
            contrat.setStartDate(contratDto.getStartDate());
            contrat.setStatus(contratDto.getStatus());
            contrat.setContractType(contratDto.getContractType());

            contratRepository.save(contrat);
            return ResponseEntity.ok("Contrat updated successfully");
        } catch (Exception e) {
            log.error("Update failed for contrat: {}", id, e);
            throw new RuntimeException("Failed to update contrat", e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllContrats() {
        try {
            return ResponseEntity.ok(contratRepository.findAll());
        } catch (Exception e) {
            log.error("Failed to retrieve all contrats", e);
            throw new RuntimeException("Failed to retrieve all contrats", e);
        }
    }
    
    private void saveUserLocally(ContratDto contratDto) {
        Contrat contrat = new Contrat();
        contrat.setEmployeeCni(contratDto.getEmployeeCni());
        contrat.setEmployeefName(contratDto.getEmployeeFName());
        contrat.setEmployeelName(contratDto.getEmployeeFName());
        contrat.setEndDate(contratDto.getEndDate());
        contrat.setStartDate(contratDto.getStartDate());
        contrat.setSalary(contratDto.getSalary());
        contrat.setStatus(contratDto.getStatus());
        contrat.setContractType(contratDto.getContractType());
        contratRepository.save(contrat);
    }
}