package ca.bc.gov.educ.api.aved.rest;

import ca.bc.gov.educ.api.aved.exception.AvedAPIRuntimeException;
import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequest;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
}
