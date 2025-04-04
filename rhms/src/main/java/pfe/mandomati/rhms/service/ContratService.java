package pfe.mandomati.rhms.service;

import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.Dto.ContratDto;

public interface ContratService {

    ResponseEntity<String> register(ContratDto contratDto);
    ResponseEntity<String> deleteContrat(Long id);
    ResponseEntity<String> updateContrat(Long id, ContratDto contratDto);
    ResponseEntity<?> getAllContrats();
    
}
