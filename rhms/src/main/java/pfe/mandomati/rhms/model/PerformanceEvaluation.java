package pfe.mandomati.rhms.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "performance_evaluations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceEvaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "employee_evaluee_id")
    private Employee employeeEvaluee;
    
    @ManyToOne
    @JoinColumn(name = "employee_evaluator_id")
    private Employee employeeEvaluator;
    
    @Column(name = "rating")
    private Integer rating;
    
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
    
    @Column(name = "date")
    private LocalDate date;
}
