package pfe.mandomati.rhms.Dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveDto {

    private Long id;

    private String employeefName;

    private String employeelName;

    private String employeeCni;

    private LocalDate startDate;

    private LocalDate endDate;

    private String type;

    private String status;
}
