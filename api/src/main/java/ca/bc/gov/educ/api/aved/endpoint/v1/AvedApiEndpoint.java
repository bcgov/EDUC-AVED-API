package ca.bc.gov.educ.api.aved.endpoint.v1;

import ca.bc.gov.educ.api.aved.struct.v1.PenRequestResult;
import ca.bc.gov.educ.api.aved.struct.v1.Request;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

/**
 * The interface Pen my ed api endpoint.
 */
@RequestMapping("/api/v1/aved")
@OpenAPIDefinition(info = @Info(title = "API for Pen Aved Integration.", description = "This API exposes different endpoints for Aved.", version = "1"), security =
  {@SecurityRequirement(name =
    "OAUTH2", scopes = {"AVED_READ_PEN_REQUEST_BATCH", "AVED_WRITE_PEN_REQUEST_BATCH", "AVED_READ_PEN_COORDINATOR", "AVED_VALIDATE_PEN"})})
public interface AvedApiEndpoint {

  /**
   * Pen request mono.
   *
   * @param request the request
   * @return the mono
   */
  @PostMapping("/pen-request")
  @PreAuthorize("hasAuthority('SCOPE_AVED_PEN_REQUEST')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "PenRequestResult", implementation = PenRequestResult.class))), @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(name = "PenRequestResult", implementation = PenRequestResult.class))), @ApiResponse(responseCode = "300", description = "Multiple Choices")})
  @Tag(name = "Endpoint to request a student PEN.", description = "Endpoint to request a student PEN.")
  @Schema(name = "Request", implementation = Request.class)
  Mono<ResponseEntity<PenRequestResult>> penRequest(@Validated @RequestBody Request request);

}
