package ca.bc.gov.educ.api.aved.endpoint.v1;

import ca.bc.gov.educ.api.aved.struct.v1.*;
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

/**
 * The interface Pen my ed api endpoint.
 */
@RequestMapping("/api/v1/aved")
@OpenAPIDefinition(info = @Info(title = "API for Pen AVED Integration.", description = "This API exposes different endpoints for AVED.", version = "1"), security =
  {@SecurityRequirement(name =
    "OAUTH2", scopes = {"AVED_PEN_REQUEST", "AVED_PEN_VALIDATION"})})
public interface AvedApiEndpoint {

  /**
   * Pen request mono.
   *
   * @param request the request
   * @return the mono
   */
  @PostMapping("/bcsc-pen-request")
  @PreAuthorize("hasAuthority('SCOPE_AVED_PEN_REQUEST')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "BcscPenRequestResult", implementation = BcscPenRequestResult.class))), @ApiResponse(responseCode = "300", description = "Multiple Choices")})
  @Tag(name = "Endpoint to request a student PEN via BCSC.", description = "Endpoint to request a student PEN via BCSC.")
  @Schema(name = "BcscPenRequest", implementation = BcscPenRequest.class)
  ResponseEntity<BcscPenRequestResult> bcscRequest(@Validated @RequestBody BcscPenRequest request);

  /**
   * Pen request mono.
   *
   * @param request the request
   * @return the mono
   */
  @PostMapping("/pen-request")
  @PreAuthorize("hasAuthority('SCOPE_AVED_PEN_REQUEST')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "PenRequestResult", implementation = PenRequestResult.class))), @ApiResponse(responseCode = "300", description = "Multiple Choices")})
  @Tag(name = "Endpoint to request a student PEN.", description = "Endpoint to request a student PEN.")
  @Schema(name = "PENRequest", implementation = PenRequest.class)
  ResponseEntity<PenRequestResult> penRequest(@Validated @RequestBody PenRequest request);

  /**
   * Pen validation mono.
   *
   * @param request the request
   * @return the mono
   */
  @PostMapping("/pen-validation")
  @PreAuthorize("hasAuthority('SCOPE_AVED_PEN_VALIDATION')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(name = "PenValidationResult", implementation = PenValidationResult.class)))})
  @Tag(name = "Endpoint to validate a student PEN.", description = "Endpoint to validate a student PEN.")
  @Schema(name = "PENValidationRequest", implementation = PenValidationRequest.class)
  ResponseEntity<PenValidationResult> penValidation(@Validated @RequestBody PenValidationRequest request);

}
