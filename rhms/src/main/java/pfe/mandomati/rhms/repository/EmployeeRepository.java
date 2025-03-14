package pfe.mandomati.rhms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.enums.Job;
import pfe.mandomati.rhms.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByCniOrCnssNumber(String cni, String cnssNumber);
    List<Employee> findByJob(Job job);

}
