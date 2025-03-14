package pfe.mandomati.rhms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByUserIdOrCniOrCnssNumber(Long userId, String cni, String cnssNumber);

}
