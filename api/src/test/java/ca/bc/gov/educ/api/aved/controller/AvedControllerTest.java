package ca.bc.gov.educ.api.aved.controller;

import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.aved.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AvedControllerTest {

  private final String guid = UUID.randomUUID().toString();
  @Autowired
  WebClient webClient;
  @Autowired
  ApplicationProperties props;
  /**
   * The Mock mvc.
   */
  @Autowired
  private MockMvc mockMvc;
  @Mock
  private WebClient.RequestHeadersSpec requestHeadersMock;
  @Mock
  private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
  @Mock
  private WebClient.RequestBodySpec requestBodyMock;
  @Mock
  private WebClient.RequestBodyUriSpec requestBodyUriMock;
  @Mock
  private WebClient.ResponseSpec responseMock;
  @Autowired
  RestUtils restUtils;

  @AfterEach
  void tearDown() {
    reset(this.restUtils);
  }

  /**
   * Sets up.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void performLogin_givenValidPayload_shouldReturnNoContent() throws Exception {
    PenValidationRequest penValidationRequest = createPenValidationRequest();
    PenMatchResult result = createPenMatchResponse();
    //Mono<ResponseEntity<PenMatchResult>> response = Mono.just(ResponseEntity.ok(result));
    //Mockito.when(this.restUtils.postToMatchAPI(ArgumentMatchers.any())).thenReturn(response);
    //when(restUtils.postToMatchAPI(ArgumentMatchers.any())).thenAnswer((Answer<Mono<ResponseEntity<PenMatchResult>>>) invocation -> Mono.fromFuture(CompletableFuture.supplyAsync(() -> ResponseEntity.ok().body(result))));

    final PenMatchStudent student = this.createPenMatchStudent();
    final PenMatchResult penMatchResult = this.createPenMatchResponse();
    when(this.webClient.post()).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.uri(this.props.getPenMatchApiURL() + "/pen-match")).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.header(any(), any())).thenReturn(this.returnMockBodySpec());
    when(this.requestBodyMock.body(any(), (Class<?>) any(Object.class))).thenReturn(this.requestHeadersMock);
    when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
    when(this.requestHeadersMock.exchangeToMono(any())).thenReturn(Mono.just(ResponseEntity.ok().body(penMatchResult)));

    this.mockMvc
      .perform(post("/api/v1/aved/pen-validation")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "AVED_PEN_VALIDATION")))
        .content(JsonUtil.getJsonStringFromObject(penValidationRequest))
        .contentType(APPLICATION_JSON))
      .andDo(print()).andExpect(status().isOk())
      .andExpect( content().json("{\"demographicsMatch\":  \"true\"}"));

//    verify(this.webClient, atMost(invocations + 1)).put();
  }

  private PenValidationRequest createPenValidationRequest(){
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

  private PenMatchStudent createPenMatchStudent() {
    PenMatchStudent penMatchStudent = new PenMatchStudent();
    penMatchStudent.setPen("123456789");
    penMatchStudent.setDob("19980101");
    penMatchStudent.setSex("M");
    penMatchStudent.setSurname("SMITH");
    penMatchStudent.setGivenName("JOHN");
    penMatchStudent.setMiddleName("WAYNE");
    penMatchStudent.setPostal("V0H1A0");
    return penMatchStudent;
  }

  private PenMatchResult createPenMatchResponse() {
    PenMatchResult penMatchResult = new PenMatchResult();
    penMatchResult.setPenStatus("AA");
    return penMatchResult;
  }

  private WebClient.RequestBodySpec returnMockBodySpec() {
    return this.requestBodyMock;
  }

}
