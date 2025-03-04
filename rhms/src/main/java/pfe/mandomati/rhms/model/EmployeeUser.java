package pfe.mandomati.rhms.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUser {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "cni", length = 50, unique = true)
    private String cni;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "cnss_number", length = 50)
    private String cnssNumber;

    @Column(name = "role", length = 50)
    private String role;


}
