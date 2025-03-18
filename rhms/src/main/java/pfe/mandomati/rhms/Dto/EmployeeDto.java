package pfe.mandomati.rhms.Dto;

import java.time.LocalDate;
import pfe.mandomati.rhms.enums.Job;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeDto {

    private Long id;
    
    private String cni;

    private String firstName;

    private String lastName;

    private String telephone;

    private String adress;
    
    private LocalDate hireDate;

    private String cnssNumber;

    private Job job;

}
