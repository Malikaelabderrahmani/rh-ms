package pfe.mandomati.rhms.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {

    private Long id;
    
    private String name;
}

