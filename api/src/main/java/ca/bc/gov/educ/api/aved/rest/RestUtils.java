package ca.bc.gov.educ.api.aved.rest;

import ca.bc.gov.educ.api.aved.exception.AvedAPIRuntimeException;
import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.struct.v1.BcscPenRequest;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequest;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.aved.struct.v1.soam.SoamLoginEntity;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

/**
 * The type Rest utils.
 */
@Component
@Slf4j
public class RestUtils {

  public static final String NULL_BODY_FROM = "null body from ";

  private static final String CONTENT_TYPE = "Content-Type";

  /**
   * The Props.
   */
  private final ApplicationProperties props;
  /**
   * The Web client.
   */
  private final WebClient webClient;

  /**
   * Instantiates a new Rest utils.
   *
   * @param props     the props
   * @param webClient the web client
   */
  @Autowired
  public RestUtils(final ApplicationProperties props, final WebClient webClient) {
    this.props = props;
    this.webClient = webClient;
  }

  private void logSuccess(final String s, final String... args) {
    log.info("API call success :: {} {}", s, args);
  }

  private String getErrorMessageString(final HttpStatus status, final String body) {
    return "Unexpected HTTP return code: " + status + " error message: " + body;
  }

  public Optional<PenMatchResult> postToMatchAPI(PenMatchStudent request) {
    try {
      val response = this.webClient.post()
        .uri(this.props.getPenMatchApiURL())
        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(request), PenMatchStudent.class)
        .retrieve()
        .bodyToMono(PenMatchResult.class)
        .doOnSuccess(entity -> {
          if (entity != null) {
            this.logSuccess(entity.toString());
          }
        })
        .block();
      if (response == null) {
        throw new AvedAPIRuntimeException(this.getErrorMessageString(HttpStatus.INTERNAL_SERVER_ERROR, NULL_BODY_FROM +
          "pen match get call."));
      }
      return Optional.of(response);
    } catch (final WebClientResponseException e) {
      throw new AvedAPIRuntimeException(this.getErrorMessageString(e.getStatusCode(), e.getResponseBodyAsString()));
    }
  }

  public Optional<SoamLoginEntity> performLink(BcscPenRequest servicesCard) {
    String url = props.getSoamApiURL();
    final String correlationID = logAndGetCorrelationID(servicesCard.getDid(), url, HttpMethod.POST.toString());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("correlationID", correlationID);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("birthDate", servicesCard.getBirthDate());
    map.add("did", servicesCard.getDid());
    map.add("email", servicesCard.getEmail());
    map.add("gender", servicesCard.getGender());
    map.add("givenName", servicesCard.getGivenName());
    map.add("givenNames", servicesCard.getGivenNames());
    map.add("identityAssuranceLevel", servicesCard.getIdentityAssuranceLevel());
    map.add("postalCode", servicesCard.getPostalCode());
    map.add("surname", servicesCard.getSurname());
    map.add("userDisplayName", servicesCard.getUserDisplayName());

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    try {
      val response = this.webClient.post()
        .uri(this.props.getSoamApiURL())
        .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(request), HttpEntity.class)
        .retrieve()
        .bodyToMono(SoamLoginEntity.class)
        .doOnSuccess(entity -> {
          if (entity != null) {
            this.logSuccess(entity.toString());
          }
        })
        .block();
      if (response == null) {
        throw new AvedAPIRuntimeException(this.getErrorMessageString(HttpStatus.INTERNAL_SERVER_ERROR, NULL_BODY_FROM +
          "SOAM link get call."));
      }
      return Optional.of(response);
    } catch (final WebClientResponseException e) {
      throw new AvedAPIRuntimeException(this.getErrorMessageString(e.getStatusCode(), e.getResponseBodyAsString()));
    }
  }

  private String logAndGetCorrelationID(String identifierValue, String url, String httpMethod) {
    final String correlationID = UUID.randomUUID().toString();
    MDC.put("correlation_id", correlationID);
    MDC.put("user_guid", identifierValue);
    MDC.put("client_http_request_url", url);
    MDC.put("client_http_request_method", httpMethod);
    log.info("Correlation id for did=" + identifierValue + " is=" + correlationID);
    MDC.clear();
    return correlationID;
  }
}
