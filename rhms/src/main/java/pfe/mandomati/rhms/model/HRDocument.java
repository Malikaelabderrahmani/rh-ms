package pfe.mandomati.rhms.model;

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
@Table(name = "hr_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HRDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employee_cni", length = 50)
    private String employeeCni;
    
    @Column(name = "document_type", length = 100)
    private String documentType;
    
    @Column(name = "path", length = 255)
    private String path;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
