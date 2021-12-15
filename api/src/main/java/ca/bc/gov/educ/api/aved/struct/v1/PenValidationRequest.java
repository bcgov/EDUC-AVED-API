package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class PenValidationRequest extends PenRequest {

  /**
   * The PEN.
   */
  @Size(max = 9)
  @NotBlank
  String pen;

}
