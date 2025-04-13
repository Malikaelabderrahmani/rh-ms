package pfe.mandomati.rhms.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResignationRequestDto {

    private Long id;

    private String employeefName;

    private String employeelName;

    private String employeeCni;

    private String reason;

    private LocalDate requestDate;

    private String status;
}
