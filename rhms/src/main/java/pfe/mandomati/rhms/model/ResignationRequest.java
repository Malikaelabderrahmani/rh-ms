package pfe.mandomati.rhms.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resignation_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResignationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_cni", nullable = false, unique = true)
    private String employeeCni;

    @Column(name = "employee_fname", nullable = false, length = 100)
    private String employeeFName;

    @Column(name = "employee_lname", nullable = false, length = 100)
    private String employeeLName;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status; // PENDING, APPROVED, REJECTED
}
