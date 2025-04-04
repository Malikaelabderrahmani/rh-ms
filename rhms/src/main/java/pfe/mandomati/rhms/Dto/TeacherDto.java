package pfe.mandomati.rhms.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherDto {

    private Long id;

    private String cni;

    private LocalDate hireDate;

    private String cnssNumber;

    private String speciality;

    private String username;

    private String lastname;

    private String firstname;

    private String email;

    private RoleDto role;

    private String password;

    private boolean status;

    private String address;

    private LocalDate birthDate;

    private String city;

    private LocalDateTime createdAt;

}
