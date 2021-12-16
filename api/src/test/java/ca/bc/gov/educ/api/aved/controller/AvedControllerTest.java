package ca.bc.gov.educ.api.aved.controller;

import ca.bc.gov.educ.api.aved.AvedApiApplicationTests;
import ca.bc.gov.educ.api.aved.properties.ApplicationProperties;
import ca.bc.gov.educ.api.aved.rest.RestUtils;
import ca.bc.gov.educ.api.aved.struct.v1.PenValidationRequest;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchResult;
import ca.bc.gov.educ.api.aved.struct.v1.penmatch.PenMatchStudent;
import ca.bc.gov.educ.api.aved.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

class AvedControllerTest extends AvedApiApplicationTests {

  @Autowired
  ApplicationProperties props;

  @Autowired
  RestUtils restUtils;

  @AfterEach
  void tearDown() {
    reset(this.restUtils);
  }

  @Test
  void performLogin_givenValidPayload_shouldReturnNoContent() throws Exception {
    PenValidationRequest penValidationRequest = createPenValidationRequest();
    PenMatchResult result = createPenMatchResponse();
    //Mono<ResponseEntity<PenMatchResult>> response = Mono.just(ResponseEntity.ok(result));
    //Mockito.when(this.restUtils.postToMatchAPI(ArgumentMatchers.any())).thenReturn(response);
    when(restUtils.postToMatchAPI(any())).thenAnswer((Answer<Mono<ResponseEntity<PenMatchResult>>>) invocation -> Mono.fromFuture(CompletableFuture.supplyAsync(() -> ResponseEntity.ok().body(result))));

    final PenMatchStudent student = this.createPenMatchStudent();
    final PenMatchResult penMatchResult = this.createPenMatchResponse();
 //   when(this.client.post()).thenAnswer((Answer<org.springframework.test.web.reactive.server.WebTestClient.RequestBodyUriSpec>) spec -> this.requestBodyUriMock);

 //   when(this.requestBodyUriMock.uri(this.props.getPenMatchApiURL() + "/api/v1/aved/pen-validation")).thenAnswer((Answer<org.springframework.test.web.reactive.server.WebTestClient.RequestBodyUriSpec>) spec -> this.requestBodyUriMock);
    //when(this.requestBodyUriMock.header(any(), any())).thenReturn(this.returnMockBodySpec());
 //   when(this.requestBodyMock.body(any(), (Class<?>) any(Object.class))).thenReturn(this.requestHeadersMock);
//    when(this.requestHeadersMock.exchange()).thenReturn(Mono.just(ResponseEntity.ok().body(penMatchResult)));

//    String response = client.post()
//      .uri("/api/v1/aved/pen-validation")
//      .body(BodyInserters.fromValue(JsonUtil.getJsonStringFromObject(penValidationRequest)))
//      .accept(MediaType.APPLICATION_JSON)
//      .exchange()
//      .expectStatus().isOk()
//      .returnResult(String.class)
//      .getResponseBody()
//      .blockFirst();

    this.client
      .mutateWith(mockJwt().jwt((jwt) -> jwt.claim("scope", "AVED_PEN_VALIDATION")))
      .mutateWith(csrf())
      .post().uri("/api/v1/aved/pen-validation")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(BodyInserters.fromValue(JsonUtil.getJsonStringFromObject(penValidationRequest)))
      .accept(MediaType.APPLICATION_FORM_URLENCODED)
      .exchange()
      .expectStatus().isNoContent();

//    this.webTestClient.post().uri("/api/v1/aved/pen-validation")
//        .with(jwt().jwt((jwt) -> jwt.claim("scope", "AVED_PEN_VALIDATION")))
//        .content(JsonUtil.getJsonStringFromObject(penValidationRequest))
//        .contentType(APPLICATION_JSON))
//      .andDo(print()).andExpect(status().isOk())
//      .andExpect( content().json("{\"demographicsMatch\":  \"true\"}"));

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

//  private WebClient.RequestBodySpec returnMockBodySpec() {
//    return this.requestBodyMock;
//  }

}
