package ca.bc.gov.educ.api.aved.support;

import ca.bc.gov.educ.api.aved.rest.RestUtils;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Mock Class for tests
 */
@Profile("test")
@Configuration
public class MockConfiguration {
  @Bean
  @Primary
  public WebClient webClient() {
    return Mockito.mock(WebClient.class);
  }

  @Bean
  @Primary
  public WebTestClient webTestClient() {
    return Mockito.mock(WebTestClient.class);
  }

}
