package pfe.mandomati.rhms.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rh")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RH {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // @OneToMany(mappedBy = "rh", cascade = CascadeType.ALL)
    // private List<Contrat> contrats;
    
    // @OneToMany(mappedBy = "rh", cascade = CascadeType.ALL)
    // private List<Leave> leaves;

}
