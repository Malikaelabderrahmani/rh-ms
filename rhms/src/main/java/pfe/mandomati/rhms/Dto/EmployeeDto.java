package pfe.mandomati.rhms.Dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeDto {
    
    private String cni;

    private String firstName;

    private String lastName;

    private String telephone;

    private String adress;
    
    private LocalDate hireDate;

    private String cnssNumber;

    private String job;

}
