package ca.bc.gov.educ.api.aved.rest;

import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import lombok.val;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@SuppressWarnings("java:S5778")
public class RestUtilsTest {
  private static final String correlationID = UUID.randomUUID().toString();
  @Autowired
  RestUtils restUtils;

  @Autowired
  WebClient webClient;

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

  @Before
  public void setUp() {
    when(this.webClient.get()).thenReturn(this.requestHeadersUriMock);
    openMocks(this);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testGetPenMatchResult_givenAPICallSuccess_shouldReturnPenMatchResult() {
    final PenMatchStudent student = this.createPenMatchStudent();
    final PenMatchResult penMatchResult = this.createPenMatchResponse();
    when(this.webClient.post()).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.uri(this.props.getPenMatchApiURL() + "/pen-match")).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.header(any(), any())).thenReturn(this.returnMockBodySpec());
    when(this.requestBodyMock.body(any(), (Class<?>) any(Object.class))).thenReturn(this.requestHeadersMock);
    when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
    when(this.responseMock.bodyToMono(PenMatchResult.class)).thenReturn(Mono.just(penMatchResult));

    val response = this.restUtils.postToMatchAPI(student);
    assertThat(response).isNotNull();
    assertThat(response.isPresent()).isTrue();
    assertThat(response.get().getPenStatus()).isNotNull();
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
