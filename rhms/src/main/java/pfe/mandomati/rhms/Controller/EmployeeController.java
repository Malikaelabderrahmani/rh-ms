package pfe.mandomati.rhms.Controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
public class EmployeeController {

    @GetMapping("/hello")
    public String getMethodName() {
        return "Hello World";
    }
    
}
