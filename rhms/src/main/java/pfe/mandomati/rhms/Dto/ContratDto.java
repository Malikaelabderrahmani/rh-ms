package pfe.mandomati.rhms.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContratDto {

    private Long id;
    private String employeeCni;
    private String employeeFName;
    private String employeeLName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salary;
    private String contractType;
    private String status;
}

