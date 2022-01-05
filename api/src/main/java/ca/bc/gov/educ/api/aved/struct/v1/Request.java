package ca.bc.gov.educ.api.aved.struct.v1;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
  @Pattern(regexp = "^((19|20)\\d\\d)(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])$")
  @Size(max = 8, min = 8)
  @NotNull(message = "birthDate cannot be null")
  @NotBlank
  String birthDate;
  /**
   * The Gender.
   */
  @NotNull(message = "gender cannot be null")
  @Size(max = 1, min = 1)
  @Pattern(regexp = "[MFUX]")
  @NotBlank
  String gender;
  /**
   * The Postal code.
   */
  @Size(max = 6, min = 6)
  @Pattern(regexp = "^([A-Z]\\d[A-Z]\\d[A-Z]\\d|)$")
  @NotNull(message = "postalCode cannot be null")
  @NotBlank
  String postalCode;

}
