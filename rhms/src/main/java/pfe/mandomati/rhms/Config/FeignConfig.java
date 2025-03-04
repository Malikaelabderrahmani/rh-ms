package pfe.mandomati.rhms.Config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "pfe.mandomati.rhms.repository")
public class FeignConfig {
}
