package ca.bc.gov.educ.api.aved.rest;

import ca.bc.gov.educ.api.aved.AvedApiApplicationTests;
import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import lombok.val;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ActiveProfiles("test")
@SpringBootTest
@SuppressWarnings("java:S5778")
class RestUtilsTest extends AvedApiApplicationTests {
  private static final String correlationID = UUID.randomUUID().toString();
  @Autowired
  RestUtils restUtils;

  @Autowired
  ApplicationProperties props;
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

  @BeforeEach
  public void setUp() throws Exception {
    //openMocks(this);
  }

  @AfterEach
  public void tearDown() throws Exception {
  }

  @Test
  void testGetPenMatchResult_givenAPICallSuccess_shouldReturnPenMatchResult() {
    final PenMatchStudent student = this.createPenMatchStudent();
    final PenMatchResult penMatchResult = this.createPenMatchResponse();
    when(this.webClient.post()).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.uri(this.props.getPenMatchApiURL() + "/pen-match")).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.header(any(), any())).thenReturn(this.returnMockBodySpec());
    when(this.requestBodyMock.body(any(), (Class<?>) any(Object.class))).thenReturn(this.requestHeadersMock);
    when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
    when(this.requestHeadersMock.exchangeToMono(any())).thenReturn(Mono.just(ResponseEntity.ok().body(penMatchResult)));
    //when(this.webClient.post()).thenAnswer((Answer<WebClient.RequestBodyUriSpec>) spec -> this.requestBodyUriMock);


    val response = this.restUtils.postToMatchAPI(student);
    assertThat(response).isNotNull();
    assertThat(response.block().getBody()).isNotNull();
    assertThat(response.block().getBody().getPenStatus()).isNotNull();
  }

  private WebClient.RequestBodySpec returnMockBodySpec() {
    return this.requestBodyMock;
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

}
