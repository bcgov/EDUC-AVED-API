package ca.bc.gov.educ.api.aved.rest;

import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequest;
import ca.bc.gov.educ.api.aved.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jboss.threads.EnhancedQueueExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The type Rest utils.
 */
@Component
@Slf4j
public class RestUtils {

  /**
   * The constant CONTENT_TYPE.
   */
  private static final String CONTENT_TYPE = "Content-Type";

  private static final String SUCCESS_STRING = "API call to postPenRequestToBatchAPI success :: {}, for surname :: {}";

  private static final String FAILURE_STRING = "API call to postPenRequestToBatchAPI failure :: {}, for surname :: {}";

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

  public Mono<ResponseEntity<PenRequestResult>> postPenRequestToBatchAPI(final PenRequest request) {
    return this.webClient.post()
      .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request").build())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(request), PenRequest.class)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK) || response.statusCode().equals(HttpStatus.CREATED)) {
          log.info(SUCCESS_STRING, response.rawStatusCode(), request.getLegalSurname());
          return response.toEntity(PenRequestResult.class);
        } else if (response.statusCode().equals(HttpStatus.MULTIPLE_CHOICES)) {
          log.info(SUCCESS_STRING, response.rawStatusCode(), request.getLegalSurname());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        } else {
          log.error(FAILURE_STRING, response.rawStatusCode(), request.getLegalSurname());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }

  public Mono<ResponseEntity<PenRequestResult>> postPenRequestToBatchAPI(final PenValidationRequest request) {
    return this.webClient.post()
      .uri(this.props.getPenRegBatchApiUrl(), uriBuilder -> uriBuilder.path("/pen-request").build())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(request), PenRequest.class)
      .exchangeToMono(response -> {
        if (response.statusCode().equals(HttpStatus.OK) || response.statusCode().equals(HttpStatus.CREATED)) {
          log.info(SUCCESS_STRING, response.rawStatusCode(), request.getLegalSurname());
          return response.toEntity(PenRequestResult.class);
        } else if (response.statusCode().equals(HttpStatus.MULTIPLE_CHOICES)) {
          log.info(SUCCESS_STRING, response.rawStatusCode(), request.getLegalSurname());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        } else {
          log.error(FAILURE_STRING, response.rawStatusCode(), request.getLegalSurname());
          return Mono.just(ResponseEntity.status(response.statusCode()).build());
        }
      });
  }

  public Mono<ResponseEntity<PenMatchResult>> postToMatchAPI(final PenMatchStudent request) {
    return this.webClient.post()
      .uri(this.props.getPenMatchApiURL(), uriBuilder -> uriBuilder.path("/pen-match").build())
      .header(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .body(Mono.just(request), PenMatchStudent.class)
      .exchangeToMono(response ->
          Mono.just(ResponseEntity.status(response.statusCode()).build())
      );
  }
}
