package ca.bc.gov.educ.api.aved.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ApplicationPropertiesTest {
  @Autowired
  ApplicationProperties properties;

  @Test
  void testPropValuesToBeNotNull() {
    assertThat(this.properties).isNotNull();
    assertThat(this.properties.getPenMatchApiURL()).isNotNull();
    assertThat(this.properties.getClientID()).isNotNull();
    assertThat(this.properties.getClientSecret()).isNotNull();
    assertThat(this.properties.getPenRegBatchApiUrl()).isNotNull();
    assertThat(this.properties.getTokenURL()).isNotNull();
  }
}
