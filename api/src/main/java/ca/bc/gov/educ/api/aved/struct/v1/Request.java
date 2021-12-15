package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * The type Request.
 */
@Data
public class Request {

  /**
   * The Birth date.
   */
  @Size(max = 8, min = 8)
  @NotBlank
  String birthDate;
  /**
   * The Gender.
   */
  @NotBlank
  @Size(max = 1, min = 1)
  @Pattern(regexp = "[MFUX]")
  String gender;
  /**
   * The Postal code.
   */
  @Size(max = 6, min = 6)
  @Pattern(regexp = "^([A-Z]\\d[A-Z]\\d[A-Z]\\d|)$")
  @NotBlank
  String postalCode;

}
