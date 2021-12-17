package ca.bc.gov.educ.api.aved.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Application properties.
 */
@Component
@Getter
@Setter
public class ApplicationProperties {

  /**
   * The constant API_NAME.
   */
  public static final String API_NAME = "AVED_API";
  /**
   * The Client id.
   */
  @Value("${client.id}")
  private String clientID;
  /**
   * The Client secret.
   */
  @Value("${client.secret}")
  private String clientSecret;
  /**
   * The Token url.
   */
  @Value("${url.token}")
  private String tokenURL;

  /**
   * The Pen match api url.
   */
  @Value("${url.api.pen.match}")
  private String penMatchApiURL;

  /**
   * The Pen match api url.
   */
  @Value("${url.api.soam}")
  private String soamApiURL;
}
