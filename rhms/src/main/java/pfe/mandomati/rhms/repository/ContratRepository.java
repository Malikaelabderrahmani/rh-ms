package pfe.mandomati.rhms.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.Contrat;

public interface ContratRepository extends JpaRepository<Contrat, Long>  {
    
    boolean existsByEmployeeCniAndStartDateAndEndDate(String employeeCni, LocalDate startDate, LocalDate endDate);

}
