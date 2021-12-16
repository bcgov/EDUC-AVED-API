package ca.bc.gov.educ.api.aved.service;

import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.service.v1.AvedService;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class AvedServiceTest {

  private static final String correlationID = UUID.randomUUID().toString();
  @Autowired
  AvedService service;

  @Autowired
  ApplicationProperties props;

  @MockBean
  RestUtils restUtils;

  @Before
  public void setUp() {
    openMocks(this);
  }

  @Test
  public void testPerformLogin_GivenDigitalIdExistAndServiceCardDoesNot_ShouldUpdateDigitalIdAndCreateServicesCard() {
    final PenValidationRequest student = this.createPenValidationRequest();
    final PenMatchResult result = this.createPenMatchResponse();
    when(this.restUtils.postToMatchAPI(any())).thenReturn(Mono.just(ResponseEntity.ok(result)));
    this.service.validatePenRequestDetail(student);
    verify(this.restUtils, times(1)).postToMatchAPI(any());
  }

  private PenValidationRequest createPenValidationRequest() {
    PenValidationRequest request = new PenValidationRequest();
    request.setPen("123456789");
    request.setLegalSurname("SMITH");
    request.setLegalGivenName("JOHN");
    request.setLegalMiddleName("WAYNE");
    request.setBirthDate("19800101");
    request.setGender("M");
    request.setPostalCode("V1V1V1");

    return request;
  }

  private PenMatchResult createPenMatchResponse() {
    PenMatchResult penMatchResult = new PenMatchResult();
    penMatchResult.setPenStatus("AA");
    return penMatchResult;
  }
}
