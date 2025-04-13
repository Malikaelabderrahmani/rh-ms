package pfe.mandomati.rhms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pfe.mandomati.rhms.model.ResignationRequest;

public interface ResignationRequestRepository extends JpaRepository<ResignationRequest, Long> {
    boolean existsByEmployeeCni(String employeeCni);
    ResignationRequest findByEmployeeCni(String employeeCni);
}
