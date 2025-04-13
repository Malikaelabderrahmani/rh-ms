package pfe.mandomati.rhms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    boolean existsByUserIdOrCniOrCnssNumber(Long userId, String cni, String cnssNumber);
    boolean existsByCni(String cni);

}
