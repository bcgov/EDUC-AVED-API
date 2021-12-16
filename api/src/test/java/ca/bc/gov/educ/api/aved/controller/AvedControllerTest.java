package ca.bc.gov.educ.api.aved.controller;

import ca.bc.gov.educ.api.aved.AvedApiApplicationTests;
import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.aved.util.JsonUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AvedControllerTest  {

  @Autowired
  ApplicationProperties props;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  RestUtils restUtils;

  @Autowired
  WebClient webClient;

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


  @Test
  public void performLogin_givenValidPayload_shouldReturnNoContent() throws Exception {
    String responseString = "{\"demographicsMatchPEN\":\"true\"}";
    PenValidationRequest penValidationRequest = createPenValidationRequest();
    PenMatchResult result = createPenMatchResponse();
    final PenMatchStudent student = this.createPenMatchStudent();
    final PenMatchResult penMatchResult = this.createPenMatchResponse();
    when(this.webClient.post()).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.uri(this.props.getPenMatchApiURL() + "/pen-match")).thenReturn(this.requestBodyUriMock);
    when(this.requestBodyUriMock.header(any(), any())).thenReturn(this.returnMockBodySpec());
    when(this.requestBodyMock.body(any(), (Class<?>) any(Object.class))).thenReturn(this.requestHeadersMock);
    when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
    when(this.responseMock.bodyToMono(PenMatchResult.class)).thenReturn(Mono.just(penMatchResult));

    var mockResult = this.mockMvc
      .perform(post("/api/v1/aved/pen-validation")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "AVED_PEN_VALIDATION")))
        .content(JsonUtil.getJsonStringFromObject(penValidationRequest))
        .contentType(APPLICATION_JSON))
      .andDo(print()).andExpect(status().isOk())
      .andReturn();

    assertThat(mockResult.getResponse().getContentAsString()).isNotNull();
    assertThat(mockResult.getResponse().getContentAsString()).isEqualTo(responseString);
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

//  private WebClient.RequestBodySpec returnMockBodySpec() {
//    return this.requestBodyMock;
//  }

}
