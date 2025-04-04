package pfe.mandomati.rhms.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pfe.mandomati.rhms.enums.Job;
@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cni", length = 50, unique = true)
    private String cni;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "telephone", length = 20, unique = true, nullable = false)
    private String telephone;

    @Column(name = "adress", length = 50)
    private String adress;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "cnss_number", length = 50)
    private String cnssNumber;

   @Enumerated(EnumType.STRING) // Stocke l'énumération sous forme de texte (ex: "CHAUFFEUR")
    @Column(name = "job", length = 50, nullable = false)
    private Job job;
}
