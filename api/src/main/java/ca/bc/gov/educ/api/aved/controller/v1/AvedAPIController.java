package ca.bc.gov.educ.api.aved.controller.v1;

import ca.bc.gov.educ.api.aved.endpoint.v1.AvedApiEndpoint;
import ca.bc.gov.educ.api.aved.exception.InvalidPayloadException;
import ca.bc.gov.educ.api.aved.exception.errors.ApiError;
import ca.bc.gov.educ.api.aved.service.v1.AvedService;
import ca.bc.gov.educ.api.aved.struct.v1.*;
import ca.bc.gov.educ.api.aved.validator.AvedPayloadValidator;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * The type Pen my ed controller.
 */
@RestController
@Slf4j
public class AvedAPIController implements AvedApiEndpoint {
  public static final String PAYLOAD_CONTAINS_INVALID_DATA = "Payload contains invalid data.";
  /**
   * The Pen my ed payload validator.
   */
  private final AvedPayloadValidator avedPayloadValidator;
  /**
   * The Pen my ed service.
   */
  private final AvedService avedService;

  /**
   * Instantiates a new Pen my ed controller.
   *
   * @param avedPayloadValidator the pen my ed payload validator
   * @param avedService          the pen my ed service
   */
  @Autowired
  public AvedAPIController(final AvedPayloadValidator avedPayloadValidator, final AvedService avedService) {
    this.avedPayloadValidator = avedPayloadValidator;
    this.avedService = avedService;
  }


  @Override
  public Mono<ResponseEntity<BcscPenRequestResult>> bcscRequest(BcscPenRequest request) {
    validateRequest(request);
    return this.avedService.postPenRequestToBatchAPI(request);
  }

  @Override
  public Mono<ResponseEntity<PenRequestResult>> penRequest(final PenRequest request) {
    validateRequest(request);
    return this.avedService.postPenRequestToBatchAPI(request);
  }

  @Override
  public Mono<ResponseEntity<PenValidationResult>> penValidation(PenValidationRequest request) {
    validateRequest(request);
    return this.avedService.validatePenRequestDetail(request);
  }

  private void validateRequest(Request request) {
    // struct validation
    val payloadErrors = this.avedPayloadValidator.validatePenRequestPayload(request);
    if (!payloadErrors.isEmpty()) {
      final ApiError error = ApiError.builder().timestamp(LocalDateTime.now()).message(PAYLOAD_CONTAINS_INVALID_DATA).status(BAD_REQUEST).build();
      error.addValidationErrors(payloadErrors);
      throw new InvalidPayloadException(error);
    }
  }

}
