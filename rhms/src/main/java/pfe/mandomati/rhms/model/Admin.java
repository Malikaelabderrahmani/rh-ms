package pfe.mandomati.rhms.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "cni", length = 50, unique = true)
    private String cni;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "cnss_number", length = 50, unique = true)
    private String cnssNumber;

    @Column(name = "position", length = 100, nullable = false)
    private String position;

}
