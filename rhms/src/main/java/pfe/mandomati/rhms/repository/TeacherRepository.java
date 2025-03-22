package pfe.mandomati.rhms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByUserIdOrCniOrCnssNumber(Long userId, String cni, String cnssNumber);
    List<Teacher> findBySpeciality(String speciality);


}
