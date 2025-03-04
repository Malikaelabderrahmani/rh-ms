package pfe.mandomati.rhms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.EnableFeignClients;

import pfe.mandomati.rhms.repository.UserClient;

@SpringBootTest
@EnableFeignClients
class RhmsApplicationTests {

    @MockBean
    private UserClient userClient;

    @Test
    void contextLoads() {
    }

}
