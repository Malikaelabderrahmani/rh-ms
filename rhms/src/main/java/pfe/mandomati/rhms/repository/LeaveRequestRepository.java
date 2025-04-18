package pfe.mandomati.rhms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pfe.mandomati.rhms.model.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>{

    boolean existsByEmployeeCniAndStartDateAndEndDate(String employeeCni, LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByEmployeeCni(String employeeCni);

}
