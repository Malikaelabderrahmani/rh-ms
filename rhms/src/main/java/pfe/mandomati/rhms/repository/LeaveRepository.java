package pfe.mandomati.rhms.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    boolean existsByEmployeeCniAndStartDateAndEndDate(String employeeCni, LocalDate startDate, LocalDate endDate);

}
