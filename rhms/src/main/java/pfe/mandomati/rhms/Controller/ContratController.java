package pfe.mandomati.rhms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pfe.mandomati.rhms.Dto.ContratDto;
import pfe.mandomati.rhms.service.ContratService;

@RestController
@RequestMapping("/contrat")
@RequiredArgsConstructor
public class ContratController {


    private final ContratService  contratService;

    @PostMapping("/register")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> register(@RequestBody ContratDto contratDto) {
        return contratService.register(contratDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> deleteContrat(@PathVariable Long id) {
        return contratService.deleteContrat(id);
    }

    @PutMapping("/edit/{id}")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<String> updateContrat(@PathVariable Long id, @RequestBody ContratDto contratDto) {
        return contratService.updateContrat(id, contratDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('RH')")
    public ResponseEntity<?> getAllContrats() {
        return contratService.getAllContrats();
    }


}
