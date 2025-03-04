package pfe.mandomati.rhms.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratDto {

    //private Long id;
    // private String employeeCni;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salary;
    private String contractType;
    
    private Long employeeId; // Référence à l'ID de l'employé
    private Long rhId; // Référence à l'ID du RH
}

