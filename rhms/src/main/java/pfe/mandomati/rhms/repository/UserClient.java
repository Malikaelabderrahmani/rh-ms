package pfe.mandomati.rhms.repository;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import pfe.mandomati.rhms.Dto.AdminD;
import pfe.mandomati.rhms.Dto.TeacherD;
import pfe.mandomati.rhms.Dto.UserDto;
@FeignClient(name = "iamms", url = "https://iamms.mandomati.com", contextId = "userClient")
public interface UserClient {
    
    @GetMapping("/api/auth/user/all")
    List<UserDto> getAllUsers(); // Récupérer tous les utilisateurs

    @GetMapping("/api/auth/user/role/admin")
    List<AdminD> getAdmins(); // Récupérer les administrateurs

    @GetMapping("/api/auth/user/role/teacher")
    List<TeacherD> getTeachers(); // Récupérer les enseignants
}
