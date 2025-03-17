package pfe.mandomati.rhms.Dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminD {

    private Long id;

    private String lastname;

    private String firstname;

    private String email;

    //private String role;

    private String address;

    private LocalDate birthDate;

    private String city;

    private String cni;

    private LocalDate hireDate;

    private String cnssNumber;

    private String position;
}
