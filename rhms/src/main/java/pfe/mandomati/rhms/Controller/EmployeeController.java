package main.java.pfe.mandomati.rhms.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
@RestController
@RequestMapping("/hey")
public class EmployeeController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello Malika";
    }
}
