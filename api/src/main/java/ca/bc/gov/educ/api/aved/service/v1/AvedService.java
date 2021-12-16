package ca.bc.gov.educ.api.aved.service.v1;

import ca.bc.gov.educ.api.aved.exception.AvedAPIRuntimeException;
import ca.bc.gov.educ.api.aved.mappers.v1.PenMatchStudentMapper;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.struct.v1.*;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

  public PenRequestResult postPenRequestToBatchAPI(final PenRequest request) {
    Optional<PenRequestResult> optional = this.restUtils.postPenRequestToBatchAPI(request);
    if(optional.isPresent()) {
      return optional.get();
    }else{
      throw new AvedAPIRuntimeException("Error occurred while calling Batch API");
    }
  }

  public BcscPenRequestResult postPenRequestToBatchAPI(final BcscPenRequest request) {
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
    Optional<PenRequestResult> optional = this.restUtils.postPenRequestToBatchAPI(penRequest);
    if(optional.isPresent()) {
      return getBcscPenRequestResult(optional.get(), request);
    }else{
      throw new AvedAPIRuntimeException("Error occurred while calling Batch API");
    }
  }

  public PenValidationResult validatePenRequestDetail(final PenValidationRequest request) {
    PenMatchStudent penMatchStudent = PenMatchStudentMapper.mapper.toPenMatchStudent(request);
    Optional<PenMatchResult> optional = this.restUtils.postToMatchAPI(penMatchStudent);
    if(optional.isPresent()) {
      return getPenValidationResult(optional.get());
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
}
