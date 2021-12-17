package ca.bc.gov.educ.api.aved.service.v1;

import ca.bc.gov.educ.api.aved.exception.AvedAPIRuntimeException;
import ca.bc.gov.educ.api.aved.mappers.v1.PenMatchStudentMapper;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.struct.v1.*;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

  public Pair<Integer, PenRequestResult> postPenRequestToBatchAPI(final PenRequest request) {
    Optional<PenMatchResult> optional = this.restUtils.postToMatchAPI(PenMatchStudentMapper.mapper.toPenMatchStudent(request));
    if(optional.isPresent()) {
      return getPenRequestResult(optional.get());
    }else{
      throw new AvedAPIRuntimeException("Error occurred while calling Batch API");
    }
  }

  public Pair<Integer, BcscPenRequestResult> postPenRequestToBatchAPI(final BcscPenRequest request) {
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
    Pair<Integer, PenRequestResult> penRequestResult = postPenRequestToBatchAPI(penRequest);
    return Pair.of(penRequestResult.getLeft(), getBcscPenRequestResult(penRequestResult.getRight(), request));
  }

  public Pair<Integer, PenValidationResult> validatePenRequestDetail(final PenValidationRequest request) {
    PenMatchStudent penMatchStudent = PenMatchStudentMapper.mapper.toPenMatchStudent(request);
    Optional<PenMatchResult> optional = this.restUtils.postToMatchAPI(penMatchStudent);
    if(optional.isPresent()) {
      return Pair.of(HttpStatus.OK.value(), getPenValidationResult(optional.get()));
    }else{
      throw new AvedAPIRuntimeException("Error occurred while calling Match API");
    }
  }

  private BcscPenRequestResult getBcscPenRequestResult(final PenRequestResult penRequestResult, final BcscPenRequest request) {
    BcscPenRequestResult bcscResult = new BcscPenRequestResult();
    bcscResult.setDid(request.getDid());
    bcscResult.setPen(penRequestResult.getPen());
    return bcscResult;
  }

  private PenValidationResult getPenValidationResult(final PenMatchResult penMatchResult) {
    PenValidationResult validationResult = new PenValidationResult();
    validationResult.setDemographicsMatchPEN("false");
    if(penMatchResult.getPenStatus() != null && penMatchResult.getPenStatus().equalsIgnoreCase("AA")) {
      validationResult.setDemographicsMatchPEN("true");
    }
    return validationResult;
  }

  private Pair <Integer, PenRequestResult> getPenRequestResult(final PenMatchResult penMatchResult) {
    if(penMatchResult == null || penMatchResult.getPenStatus() == null) {
      throw new AvedAPIRuntimeException("Error occurred while calling Match API");
    }
    PenRequestResult result = new PenRequestResult();

    switch (penMatchResult.getPenStatus()) {
      case "B1":
      case "C1":
      case "D1":
        result.setPen(penMatchResult.getMatchingRecords().get(0).getMatchingPEN());
        return Pair.of(HttpStatus.OK.value(), result);
      case "BM":
      case "CM":
      case "DM":
        return Pair.of(HttpStatus.MULTIPLE_CHOICES.value(), result);
      default:
        return Pair.of(HttpStatus.OK.value(), result);
    }
  }
}
