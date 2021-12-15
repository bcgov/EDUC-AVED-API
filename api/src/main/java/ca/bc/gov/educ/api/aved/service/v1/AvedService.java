package ca.bc.gov.educ.api.aved.service.v1;

import ca.bc.gov.educ.api.aved.mappers.v1.PenMatchStudentMapper;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.struct.v1.*;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.aved.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The type Pen my ed service.
 */
@Service
@Slf4j
public class AvedService {
  /**
   * The Rest utils.
   */
  private final RestUtils restUtils;

  /**
   * Instantiates a new Pen my ed service.
   *
   * @param restUtils the rest utils
   */
  @Autowired
  public AvedService(final RestUtils restUtils) {
    this.restUtils = restUtils;
  }

  public Mono<ResponseEntity<PenRequestResult>> postPenRequestToBatchAPI(final PenRequest request) {
    return this.restUtils.postPenRequestToBatchAPI(request).map(penRequestResultResponseEntity -> {
      val body = penRequestResultResponseEntity.getBody();
      if (body != null) {
        return ResponseEntity.status(penRequestResultResponseEntity.getStatusCode()).body(body);
      }
      return penRequestResultResponseEntity;
    });
  }

  /**
   * Post validation request
   *
   * @param request the PenValidationRequest
   * @return Mono<ResponseEntity<PenValidationResult>>
   */
  public Mono<ResponseEntity<PenValidationResult>> validatePenRequestDetail(final PenValidationRequest request) {
    PenMatchStudent penMatchStudent = PenMatchStudentMapper.mapper.toPenMatchStudent(request);
    return this.restUtils.postToMatchAPI(penMatchStudent).map(Optional::of)
      .defaultIfEmpty(Optional.empty())
      .flatMap(penMatchResultOptional -> penMatchResultOptional
        .map(penMatchResultResponseEntity ->
          Mono.just(ResponseEntity.status(penMatchResultResponseEntity.getStatusCode()).body(getPenMatchResult(penMatchResultResponseEntity.getBody())))
        )
        .orElse(Mono.error(new RuntimeException("Not found"))));
  }

  public Mono<ResponseEntity<BcscPenRequestResult>> postPenRequestToBatchAPI(final BcscPenRequest request) {
    PenRequest penRequest = new PenRequest();
    penRequest.setLegalSurname(request.getSurname());
    penRequest.setLegalGivenName(request.getGivenName());
    if (request.getGivenNames() != null && request.getGivenName() != null) {
      penRequest.setLegalMiddleName(request.getGivenNames().replaceAll(request.getGivenName(), "").trim());
    } else if (request.getGivenNames() != null) {
      penRequest.setLegalMiddleName(request.getGivenNames());
    }
    penRequest.setBirthDate(request.getBirthDate());
    penRequest.setGender(request.getGender());
    penRequest.setPostalCode(request.getPostalCode());
    return this.restUtils.postPenRequestToBatchAPI(penRequest).map(Optional::of)
      .defaultIfEmpty(Optional.empty())
      .flatMap(penRequestResultOptional -> penRequestResultOptional
        .map(penRequestResultResponseEntity ->
          Mono.just(ResponseEntity.status(penRequestResultResponseEntity.getStatusCode()).body(getBcscPenRequestResult(penRequestResultResponseEntity.getBody(), request)))
        )
        .orElse(Mono.error(new RuntimeException("Not found"))));
  }

  private BcscPenRequestResult getBcscPenRequestResult(final PenRequestResult penRequestResult, final BcscPenRequest request) {
    BcscPenRequestResult bcscResult = new BcscPenRequestResult();
    bcscResult.setDid(request.getDid());
    bcscResult.setPen(penRequestResult.getPen());
    return bcscResult;
  }

  private PenValidationResult getPenMatchResult(final PenMatchResult penMatchResult) {
    PenValidationResult validationResult = new PenValidationResult();
    validationResult.setDemographicsMatchPEN("false");
    if(penMatchResult.getPenStatus() != null && penMatchResult.getPenStatus().equalsIgnoreCase("AA")) {
      validationResult.setDemographicsMatchPEN("true");
    }
    return validationResult;
  }
}
