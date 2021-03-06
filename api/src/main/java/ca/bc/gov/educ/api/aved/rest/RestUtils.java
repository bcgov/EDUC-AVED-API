package ca.bc.gov.educ.api.aved.rest;

import ca.bc.gov.educ.api.aved.exception.AvedAPIRuntimeException;
import ca.bc.gov.educ.api.aved.exception.MultipleMatchesException;
import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.struct.v1.BcscPenRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.aved.struct.v1.soam.SoamLoginEntity;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

  public Pair<Optional<SoamLoginEntity>, HttpStatus> performLink(BcscPenRequest servicesCard) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    addValueToMapIfNotNull(map, "birthDate", servicesCard.getBirthDate());
    addValueToMapIfNotNull(map, "did", servicesCard.getDid());
    addValueToMapIfNotNull(map, "email", servicesCard.getEmail());
    addValueToMapIfNotNull(map, "gender", servicesCard.getGender());
    addValueToMapIfNotNull(map, "givenName", servicesCard.getGivenName());
    addValueToMapIfNotNull(map, "givenNames", servicesCard.getGivenNames());
    addValueToMapIfNotNull(map, "identityAssuranceLevel", servicesCard.getIdentityAssuranceLevel());
    addValueToMapIfNotNull(map, "postalCode", servicesCard.getPostalCode());
    addValueToMapIfNotNull(map, "surname", servicesCard.getSurname());
    addValueToMapIfNotNull(map, "userDisplayName", servicesCard.getUserDisplayName());

    try {
      val response = this.webClient.post()
        .uri(this.props.getSoamApiURL())
        .header(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(Mono.just(map), MultiValueMap.class)
        .retrieve()
        .onStatus(
          HttpStatus.MULTIPLE_CHOICES::equals,
          responseMultiple -> responseMultiple.bodyToMono(SoamLoginEntity.class).map(MultipleMatchesException::new))
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
      return Pair.of(Optional.of(response), HttpStatus.OK);
    } catch (final WebClientResponseException e) {
      throw new AvedAPIRuntimeException(this.getErrorMessageString(e.getStatusCode(), e.getResponseBodyAsString()));
    } catch (final MultipleMatchesException e) {
      return Pair.of(Optional.of(e.getSoamLoginEntity()), HttpStatus.MULTIPLE_CHOICES);
    }
  }

  private void addValueToMapIfNotNull(final MultiValueMap<String, String> map, final String key, final String value) {
    if (value != null) {
      map.add(key, value);
    }
  }

}
