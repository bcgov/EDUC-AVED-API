package ca.bc.gov.educ.api.aved;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public abstract class AvedApiApplicationTests {

  @Autowired
  protected WebTestClient client;
  @Autowired
  protected WebClient webClient;

}
