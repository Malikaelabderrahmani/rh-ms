package pfe.mandomati.rhms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import pfe.mandomati.rhms.model.RH;

public interface RHRepository extends JpaRepository<RH, Long> {


}
