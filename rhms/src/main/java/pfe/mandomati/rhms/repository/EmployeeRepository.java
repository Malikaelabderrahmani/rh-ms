package pfe.mandomati.rhms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByJob(String job);
}
